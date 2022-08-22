package com.logger.demo264.logrecord.expression.parse;

import com.logger.demo264.logrecord.expression.exception.ParseException;
import com.logger.demo264.logrecord.expression.handle.FunctionExpressionHandler;
import com.logger.demo264.logrecord.expression.handle.SpelExpressionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 模板解析器
 *
 * @author 风尘
 * @date 2022-07-13
 */
@RequiredArgsConstructor
public class TemplateExpressionParser implements ExpressionParser {
    private final SpelExpressionHandler spelExpressionHandler;
    private final FunctionExpressionHandler functionExpressionHandler;


    /**
     * 解析表达式
     *
     * @param expressionString 表达式文字
     * @param context          解析模板
     * @return 解析后的表达式
     * @throws ParseException 解析时出现异常
     */
    @Override
    public List<Expression> parseExpressions(String expressionString, ParserContext context) throws ParseException {
        if (expressionString.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expression> expressions = new ArrayList<>();
        String prefix = context.getExpressionPrefix();
        String suffix = context.getExpressionSuffix();
        int startIdx = 0;

        while (startIdx < expressionString.length()) {
            int prefixIndex = expressionString.indexOf(prefix, startIdx);
            if (prefixIndex >= startIdx) {
                // an inner expression was found - this is a composite
                if (prefixIndex > startIdx) {
                    expressions.add(new LiteralExpression(expressionString, expressionString.substring(startIdx, prefixIndex)));
                }
                int afterPrefixIndex = prefixIndex + prefix.length();
                int suffixIndex = skipToCorrectEndSuffix(suffix, expressionString, afterPrefixIndex);
                if (suffixIndex == -1) {
                    throw new ParseException(expressionString, null, prefixIndex,
                            "No ending suffix '" + suffix + "' for expression starting at character " +
                                    prefixIndex + ": " + expressionString.substring(prefixIndex));
                }
                if (suffixIndex == afterPrefixIndex) {
                    throw new ParseException(expressionString, null, prefixIndex,
                            "No expression defined within delimiter '" + prefix + suffix +
                                    "' at character " + prefixIndex);
                }
                // 可能是自定义函数，也可能是spel表达式
                String expr = expressionString.substring(prefixIndex + prefix.length(), suffixIndex);
                expr = StringUtils.trimAllWhitespace(expr);
                if (expr.isEmpty()) {
                    throw new ParseException(expressionString, null, prefixIndex,
                            "No expression defined within delimiter '" + prefix + suffix +
                                    "' at character " + prefixIndex);
                }

                expressions.add(doParseExpression(expressionString, expr));
                startIdx = suffixIndex + suffix.length();
            } else {
                // no more {expressions} found in string, add rest as static text
                expressions.add(new LiteralExpression(expressionString, expressionString.substring(startIdx)));
                startIdx = expressionString.length();
            }
        }

        return expressions;
    }

    /**
     * 解析时spel表达式还是自定义函数表达式
     * 自定义函数表达式：
     *
     * @param expressionString 原字符串
     * @param expr             表达式
     * @return {@link FunctionExpression} or {@link SpelExpression}
     */
    private Expression doParseExpression(String expressionString, String expr) {
        if (ParseUtils.isFunctionExpression(expr)) {
            return new FunctionExpression(expressionString, expr, functionExpressionHandler);
        }
        return new SpelExpression(expressionString, expr, spelExpressionHandler);
    }

    /**
     * 是否是后缀
     *
     * @param expressionString 表达式
     * @param pos              位置
     * @param suffix           后缀
     * @return true or false
     */
    private boolean isSuffixHere(String expressionString, int pos, String suffix) {
        int suffixPosition = 0;
        for (int i = 0; i < suffix.length() && pos < expressionString.length(); i++) {
            if (expressionString.charAt(pos++) != suffix.charAt(suffixPosition++)) {
                return false;
            }
        }
        // the expressionString ran out before the suffix could entirely be found
        return suffixPosition == suffix.length();
    }

    /**
     * 找对嵌套，例如 “{ {...}}”中第一个'{'对应的是最后一个'}'
     *
     * @param suffix           后缀
     * @param expressionString 解决的字符串
     * @param afterPrefixIndex 从第几个开始
     * @return 如果能找到正确的后缀，返回正确的位置，否则返回-1
     */
    private int skipToCorrectEndSuffix(String suffix, String expressionString, int afterPrefixIndex) {

        // Chew on the expression text - relying on the rules:
        // brackets must be in pairs: () [] {}
        // string literals are "..." or '...' and these may contain unmatched brackets
        int pos = afterPrefixIndex;
        int maxlen = expressionString.length();
        int nextSuffix = expressionString.indexOf(suffix, afterPrefixIndex);
        if (nextSuffix == -1) {
            // the suffix is missing
            return -1;
        }
        Deque<Bracket> stack = new ArrayDeque<>();
        while (pos < maxlen) {
            if (isSuffixHere(expressionString, pos, suffix) && stack.isEmpty()) {
                break;
            }
            char ch = expressionString.charAt(pos);
            switch (ch) {
                case '{':
                case '[':
                case '(':
                    stack.push(new Bracket(ch, pos));
                    break;
                case '}':
                case ']':
                case ')':
                    if (stack.isEmpty()) {
                        throw new ParseException(expressionString, null, pos, "Found closing '" + ch +
                                "' at position " + pos + " without an opening '" +
                                Bracket.theOpenBracketFor(ch) + "'");
                    }
                    Bracket p = stack.pop();
                    if (!p.compatibleWithCloseBracket(ch)) {
                        throw new ParseException(expressionString, null, pos, "Found closing '" + ch +
                                "' at position " + pos + " but most recent opening is '" + p.bracket +
                                "' at position " + p.pos);
                    }
                    break;
                case '\'':
                case '"':
                    // jump to the end of the literal
                    int endLiteral = expressionString.indexOf(ch, pos + 1);
                    if (endLiteral == -1) {
                        throw new ParseException(expressionString, null, pos,
                                "Found non terminating string literal starting at position " + pos);
                    }
                    pos = endLiteral;
                    break;
                default:
            }
            pos++;
        }
        if (!stack.isEmpty()) {
            Bracket p = stack.pop();
            throw new ParseException(expressionString, null, p.pos, "Missing closing '" +
                    Bracket.theCloseBracketFor(p.bracket) + "' for '" + p.bracket + "' at position " + p.pos);
        }
        if (!isSuffixHere(expressionString, pos, suffix)) {
            return -1;
        }
        return pos;
    }


    /**
     * This captures a type of bracket and the position in which it occurs in the
     * expression. The positional information is used if an error has to be reported
     * because the related end bracket cannot be found. Bracket is used to describe:
     * square brackets [] round brackets () and curly brackets {}
     */
    private static class Bracket {

        char bracket;

        int pos;

        Bracket(char bracket, int pos) {
            this.bracket = bracket;
            this.pos = pos;
        }

        boolean compatibleWithCloseBracket(char closeBracket) {
            if (this.bracket == '{') {
                return closeBracket == '}';
            } else if (this.bracket == '[') {
                return closeBracket == ']';
            }
            return closeBracket == ')';
        }

        static char theOpenBracketFor(char closeBracket) {
            if (closeBracket == '}') {
                return '{';
            } else if (closeBracket == ']') {
                return '[';
            }
            return '(';
        }

        static char theCloseBracketFor(char openBracket) {
            if (openBracket == '{') {
                return '}';
            } else if (openBracket == '[') {
                return ']';
            }
            return ')';
        }
    }
}
