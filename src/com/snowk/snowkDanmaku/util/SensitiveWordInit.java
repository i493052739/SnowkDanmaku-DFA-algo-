package com.snowk.snowkDanmaku.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.snowk.snowkDanmaku.SnowkDanmaku;

/**
 * ������CSDN��https://www.cnblogs.com/shihaiming/p/7048379.html 
*/

/**
 * @Description: ��ʼ�����дʿ⣬�����дʼ��뵽HashMap�У�����DFA�㷨ģ��
 * @Author : chenming
 * @Date �� 2014��4��20�� ����2:27:06
 * @version 1.0
 */
public class SensitiveWordInit {
    private String ENCODING = "GBK";    //�ַ�����
    @SuppressWarnings("rawtypes")
    public HashMap sensitiveWordMap;
    
    public SensitiveWordInit(){
        super();
    }
    
    /**
     * @author chenming 
     * @date 2014��4��20�� ����2:28:32
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public Map initKeyWord(){
        try {
            //��ȡ���дʿ�
            Set<String> keyWordSet = readSensitiveWordFile();
            //�����дʿ���뵽HashMap��
            addSensitiveWordToHashMap(keyWordSet);
            //spring��ȡapplication��Ȼ��application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    /**
              * ��ȡ���дʿ⣬�����дʷ���HashSet�У�����һ��DFA�㷨ģ�ͣ�<br>
     * 
              * �� = {
     *      isEnd = 0
              *      �� = {<br>
     *           isEnd = 1
              *           �� = {isEnd = 0
              *                �� = {isEnd = 1}
     *                }
              *           ��  = {
     *                  isEnd = 0
              *                   �� = {
     *                        isEnd = 1
     *          }
     *        }
     *      }
     *    }
     *    
              *  �� = {
     *      isEnd = 0
              *      �� = {
     *          isEnd = 0
              *          �� = {
     *              isEnd = 0
              *              �� = {
     *                   isEnd = 1
     *        }
     *       }
     *      }
     *     }
     * @author chenming 
     * @date 2014��4��20�� ����3:04:20
     * @param keyWordSet  ���дʿ�
     * @version 1.0
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void addSensitiveWordToHashMap(Set<String> keyWordSet) {
        sensitiveWordMap = new HashMap(keyWordSet.size());     //��ʼ�����д��������������ݲ���
        String key = null;  
        Map nowMap = null;
        Map<String, String> newWorMap = null;
        //����keyWordSet
        Iterator<String> iterator = keyWordSet.iterator();
        while(iterator.hasNext()){
            key = iterator.next();    //�ؼ���
            nowMap = sensitiveWordMap;
            for(int i = 0 ; i < key.length() ; i++){
                char keyChar = key.charAt(i);       //ת����char��
                Object wordMap = nowMap.get(keyChar);       //��ȡ
                
                if(wordMap != null){        //������ڸ�key��ֱ�Ӹ�ֵ
                    nowMap = (Map) wordMap;
                }
                else{     //���������򹹽�һ��map��ͬʱ��isEnd����Ϊ0����Ϊ���������һ��
                    newWorMap = new HashMap<String,String>();
                    newWorMap.put("isEnd", "0");     //�������һ��
                    nowMap.put(keyChar, newWorMap);
                    nowMap = newWorMap;
                }
                
                if(i == key.length() - 1){
                    nowMap.put("isEnd", "1");    //���һ��
                }
            }
        }
    }
 

    /**
     * ��ȡ���дʿ��е����ݣ���������ӵ�set������
     * @author chenming 
     * @date 2014��4��20�� ����2:31:18
     * @return
     * @version 1.0
     * @throws Exception 
     */
    @SuppressWarnings("resource")
    private Set<String> readSensitiveWordFile() throws Exception{
        Set<String> set = null;
        
        InputStreamReader read = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("resource/SensitiveWord.txt"),ENCODING);
        try {
            set = new HashSet<String>();
            BufferedReader bufferedReader = new BufferedReader(read);
            String txt = null;
            while((txt = bufferedReader.readLine()) != null){    //��ȡ�ļ������ļ����ݷ��뵽set��
            	if (!(SnowkDanmaku.snowkPlugin.getConfig().getStringList("remove").contains(txt))) {
            		set.add(txt);
            	}
            	List<String> addList = SnowkDanmaku.snowkPlugin.getConfig().getStringList("add");
            	for (String i : addList) {
            		set.add(i);
            	}
            }

        } catch (Exception e) {
            throw e;
        }finally{
            read.close();     //�ر��ļ���
        }
        return set;
    }
}