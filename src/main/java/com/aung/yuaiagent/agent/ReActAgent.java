package com.aung.yuaiagent.agent;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ReAct (Reasonable Agent) method of implementing an agent.
 * Build a reasoning chain of steps to execute.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public abstract class ReActAgent extends BaseAgent{
    /**
     * making current state and then confirm the next step
     * @return distinguish the next step -- true : do the next step -- false : stop the agent
     */
    public abstract boolean think();

    /**
     * execute the next step
     * @return the result of the step
     */
    public abstract  String act();

    public String step(){
        try{
            // thinking
            if(!think()){
                return "Thinking finish: no need to do any action.";
            }
            // action
            return act();
        }catch (Exception e){
            log.error("Error in step: ", e);
            return "Error in step: " + e.getMessage();
        }
    }
}
