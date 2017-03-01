package com.haodong.async;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by torch on 17-3-1.
 */

public class EventModel {
    private EventType type;
    //触发者
    private int actorId;
    //触发的载体
    private int entityType;
    private int EntityId;
    private int entityOwnerId;
    private Map<String, String > exts = new HashMap<>();

    public EventModel(){

    }
    public EventModel(EventType type){
        this.type = type;
    }
    public EventModel setExt(String key, String value){
        exts.put(key, value);
        return this;
    }

    public EventModel setExts(HashMap<String, String> exs){
        this.exts = exs;
        return this;
    }

    public EventModel setType(EventType type){
        this.type = type;
        return this;
    }

    public EventModel setEntityOwnerId(int entityOwnerId){
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public EventModel setActorId(int actorId){
        this.actorId = actorId;
        return this;
    }

    public EventModel setEntityType(int entityType){
        this.EntityId = entityType;
        return this;
    }

    public EventModel setEntityId(int entityId){
        this.EntityId = entityId;
        return this;
    }

    public String getExt(String key){
        return exts.get(key);
    }

    public EventType getType() {
        return type;
    }

    public int getActorId() {
        return actorId;
    }

    public int getEntityType() {
        return entityType;
    }

    public int getEntityId() {
        return EntityId;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public Map<String, String> getExts() {
        return exts;
    }

    @Override
    public String toString() {
        return "EventModel{" +
                "type=" + type +
                ", actorId=" + actorId +
                ", entityType=" + entityType +
                ", EntityId=" + EntityId +
                ", entityOwnerId=" + entityOwnerId +
                ", exts=" + exts +
                '}';
    }
}
