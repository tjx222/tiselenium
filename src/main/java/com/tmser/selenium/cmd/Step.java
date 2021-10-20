package com.tmser.selenium.cmd;

import com.google.common.collect.Lists;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;

public class Step {

    public static Step newStep(Function<? super WebDriver, Boolean> action){
        return new Step(action);
    }

    private Step(Function<? super WebDriver, Boolean> action){
        this.action = action;
    }

    private Step nextStep;

    private Step preStep;

    private Function<? super WebDriver, Boolean> action;

    //是否回退
    private boolean needFeedback;

    public Boolean execute(WebDriver driver){
        if(action != null && action.apply(driver)){
            if(this.nextStep != null){
                return nextStep.execute(driver);
            }
            return true;
        }
        return false;
    }

    public Step nextStep(Step nextStep){
        this.nextStep =  nextStep;
        return this;
    }

    public Step preStep(Step preStep){
        this.preStep =  preStep;
        return this;
    }

    public static Step buildStep(Function<? super WebDriver, Boolean>... actions){
        return buildStep(Lists.newArrayList(actions));
    }

    public static Step buildStep(List<Function<? super WebDriver, Boolean>> actions){
        Step first = null;
        Step pre = null;

        for (Function<? super WebDriver, Boolean> action : actions) {
            Step step = newStep(action).preStep(pre);
          if(first == null){
              first = step;
          }

          if(pre != null){
              pre.nextStep(step);
          }

          pre = step;
        }

        return  first;
    }
}
