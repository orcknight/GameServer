package com.tian.server.util;

import com.tian.server.common.TaskActionType;
import com.tian.server.model.TaskTrack;
import com.tian.server.model.TaskTrackAction;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.omg.CORBA.TRANSACTION_MODE;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by PPX on 2017/9/8.
 */
public class XmlUtil {

    public static List<TaskTrack> parseXmlToTask(){

        List<TaskTrack> taskTracks = new ArrayList<TaskTrack>();
        String filePath = XmlUtil.class.getResource("/task/Quest.xml").getPath();
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        try {
            // 通过reader对象的read方法加载Quest.xml文件,获取docuemnt对象。
            Document document = reader.read(new File(filePath));
            // 通过document对象获取根节点bookstore
            Element quest = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = quest.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            while (it.hasNext()) {
                System.out.println("=====开始遍历某个line=====");
                Element line = (Element) it.next();
                // 获取line的id
                Integer lineId = Integer.parseInt(line.attributeValue("id", "0"));

                //解析Track节点
                Iterator itt = line.elementIterator();
                while (itt.hasNext()) {
                    TaskTrack taskTrack = new TaskTrack();
                    Element track = (Element) itt.next();

                    Integer trackId = Integer.parseInt(track.attributeValue("id", "0"));
                    Integer needLv = Integer.parseInt(track.attributeValue("needLv", "1"));
                    String  desc = track.attributeValue("desc", "0");
                    Integer acceptCount = Integer.parseInt(track.attributeValue("acceptCount", "1"));

                    taskTrack.setId(trackId);
                    taskTrack.setNeedLv(needLv);
                    taskTrack.setDesc(desc);
                    taskTrack.setAcceptCount(acceptCount);

                    //遍历action
                    Iterator actionIt = track.elementIterator();
                    Integer actionId = 1;
                    while (actionIt.hasNext()) {
                        TaskTrackAction trackAction = new TaskTrackAction();
                        Element action = (Element) actionIt.next();


                        String act = action.attributeValue("act", "");
                        String mapName = action.attributeValue("mapName", "");
                        Integer npcId = Integer.parseInt(action.attributeValue("npcId", "0"));
                        Integer storyId = Integer.parseInt(action.attributeValue("storyId", "0"));
                        String actDesc = action.attributeValue("desc", "");
                        Integer rewardId = Integer.parseInt(action.attributeValue("reward", "0"));
                        Integer targetCount = Integer.parseInt(action.attributeValue("count", "1"));

                        trackAction.setId(actionId);
                        trackAction.setActionType(TaskActionType.valueOf(act.toUpperCase()));
                        trackAction.setTargetMap(mapName);
                        trackAction.setTargetId(npcId);
                        trackAction.setStoryId(storyId);
                        trackAction.setDesc(actDesc);
                        trackAction.setRewardId(rewardId);
                        trackAction.setTargetCount(targetCount);
                        actionId++;

                        List<TaskTrackAction> taskTrackAction = null;
                        if(taskTrack.getTrackActions() == null) {

                            taskTrackAction = new ArrayList<TaskTrackAction>();
                            taskTrack.setTrackActions(taskTrackAction);
                        } else {
                            taskTrackAction = taskTrack.getTrackActions();
                        }

                        taskTrackAction.add(trackAction);
                    }

                    taskTracks.add(taskTrack);

                    System.out.println("节点名：" + track.getName() + "--节点值：" + track.getStringValue());
                }
                System.out.println("=====结束遍历某一line=====");
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return taskTracks;
    }

}
