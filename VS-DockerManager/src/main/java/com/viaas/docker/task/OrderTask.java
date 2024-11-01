package com.viaas.docker.task;

import com.viaas.docker.entity.Order;

import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Chajian
 */
public class OrderTask extends BaseTask<Order> {
    //order info
   private ReentrantLock reentrantLock = new ReentrantLock();
    public OrderTask(int time, Order order) {
        super(time);
        super.t = order;
    }

    @Override
    public void start() {

    }

    @Override
    public  void run() {
        reentrantLock.lock();
        try {
            super.run();
            if(t.getState().equals("支付成功")){
                recall();
            }
        }finally {
            reentrantLock.unlock();
        }

    }

//    @Override
//    public void recall() {
//
////        reentrantLock.lock();
////        try {
////            super.recall();
////            //check order statue
////            if(order.getState().equals("支付成功")){
////                //create container and update user's info TODO
////            }
////            else if(order.getState().equals("未支付")){
////                death();
////            }
////        }finally {
////            reentrantLock.unlock();
////        }
//
//
//   }

    /**
     * 判断Task是否指定指定的order
     * @param id
     * @return
     */
    public boolean isTargetTask(int id){
        return t.getId() == id;
    }

    public String getOrderState(){
        return t.getState();
    }

    public void setOrderState(String state){
        t.setState(state);
    }

    public int getOrderId(){
        return t.getId();
    }
}
