package com.haodong.service;

import jdk.internal.util.xml.impl.Input;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import sun.text.normalizer.Trie;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by torch on 17-2-25.
 */
@Service
public class SensitiveService implements InitializingBean {
    /**
     * 实现初始化bean
     *
     * @throws Exception
     */

    @Override
    public void afterPropertiesSet() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWord.txt");
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String lineText = null;
        while ((lineText = bufferedReader.readLine()) != null) {
            addWord(lineText.trim());
        }
        reader.close();
    }

    public static void main(String[] args) {
        SensitiveService sensitiveService = new SensitiveService();
        sensitiveService.addWord("abc");
        sensitiveService.addWord("bcd");
        String res = sensitiveService.filter("   efga bc");
        System.out.println(res);
    }

    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }

        int len = text.length();
        String replaceString = "***";
        TrieNode tmpNode = rootNode;
        int begin = 0;
        int position = 0;
        StringBuilder sb = new StringBuilder();
        while (position < len) {
            char ch = text.charAt(position);
            //跳过标识符
            if(isSymbol(ch)){
                if(tmpNode == rootNode){
                    sb.append(ch);
                    begin++;
                }
                position++;
                continue;
            }
            tmpNode = tmpNode.getSubNode(ch);
            if (tmpNode == null) {
                sb.append(text.charAt(begin));
                position = begin + 1;
                begin = position;
                tmpNode = rootNode;
            } else {
                if (tmpNode.isEnd()) {
                    sb.append(replaceString);
                    //发现敏感词的后面
                    position = position + 1;
                    begin = position;
                    tmpNode = rootNode;
                } else {
                    position++;
                }
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    /**
     * 既不是东亚文字，也不是英文，可以过滤掉
     * @param c
     * @return
     */
    private boolean isSymbol(char c){
        int ic = (int)c;
        return !CharUtils.isAsciiAlphanumeric(c) && (ic <0x2E80 || ic > 0x9FFF);
    }

    /**
     * 添加一个敏感词函数到字典树中
     *
     * @param lineText
     */
    private void addWord(String lineText) {
        TrieNode tmpNode = rootNode;
        int len = lineText.length();
        for (int i = 0; i < len; i++) {
            Character ch = lineText.charAt(i);
            TrieNode node = tmpNode.getSubNode(ch);
            if (node != null) {
                tmpNode = node;
            } else {
                TrieNode newNode = new TrieNode();
                tmpNode.addSubNode(ch, newNode);
                tmpNode = newNode;
            }
            if (i == len - 1) {
                tmpNode.setEnd(true);
            }
        }
    }

    private class TrieNode {
        private boolean end = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character ch, TrieNode node) {
            subNodes.put(ch, node);
        }

        public TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        public boolean isEnd() {
            return end;
        }

        public void setEnd(boolean end) {
            this.end = end;
        }
    }

    private TrieNode rootNode = new TrieNode();
}
