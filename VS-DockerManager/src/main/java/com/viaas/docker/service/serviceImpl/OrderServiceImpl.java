package com.viaas.docker.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.viaas.docker.entity.Hardware;
import com.viaas.docker.entity.Order;
import com.viaas.docker.entity.Packet;
import com.viaas.docker.entity.dto.AddContainer;
import com.viaas.docker.entity.dto.AddOrder;
import com.viaas.docker.execption.CustomException;
import com.viaas.docker.mapper.HardwareMapper;
import com.viaas.docker.mapper.OrderMapper;
import com.viaas.docker.service.ContainerService;
import com.viaas.docker.service.OrderService;
import com.viaas.docker.service.PacketService;
import com.viaas.docker.task.*;
import com.viaas.docker.task.DTask;
import com.viaas.docker.task.OrderTask;
import com.viaas.docker.task.TaskStatus;
import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.apache.kafka.clients.consumer.ConsumerRecords;
//import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author xyl
 */
@Slf4j
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {


    @Autowired
    ContainerService containerService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    HardwareMapper hardwareMapper;
    @Autowired
    @Lazy
    PacketService packetService;
//    @Autowired
//    KafkaModel kafkaModel;
    @Autowired
    TaskThreadPool taskThreadPool;


    public TaskThreadPool getTaskThreadPool() {
        return taskThreadPool;
    }

    //消费者一秒钟轮询一次，检查是否有可消费订单
    @Scheduled(fixedRate = 1000)
    public void receiveMessage(){
//        if(!kafkaModel.isEnable())
//            return;
//        ConsumerRecords<Long, String> records = kafkaModel.getConsumer().poll(Duration.ofMillis(1));
//        for (ConsumerRecord<Long, String> record : records) {
//            AddOrder addOrder = JSON.parseObject(record.value(), AddOrder.class);
//            //创建订单
//            if(!ObjectUtils.isEmpty(addOrder.getOrder())) {
//                createOrderTask(addOrder.getOrder(), addOrder.getPacketId(), addOrder.getUserId(), addOrder.getAddContainer(), addOrder.getLifeTime());
//            }
//        }
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    //生产者生产消息
    @Override
    public Order sendMessage(AddOrder addOrder){
//        if(kafkaModel!=null) {
//            Order order = new Order();
//            order.setState("未支付");
//            order.setName("order");
//            order.setUserId(addOrder.getUserId());
//            order.setPacketId(addOrder.getPacketId());
//            orderMapper.insert(order);
//            addOrder.setOrder(order);
//            kafkaModel.getProducer().send(new ProducerRecord<Long,String>("docker-order", addOrder.getUserId(), JSON.toJSONString(addOrder)));
//            return order;
//        }
        return null;
    }



    //消费订单业务逻辑
    @Override
    public Order createOrderTask(Order order,int packetId, long userId, AddContainer addContainer,int lifeTime) {
        Packet packet = packetService.getById(packetId);//套餐
        Hardware hard = hardwareMapper.selectById(packet.getHardwareId());
        if (hard == null) {
            throw new CustomException(500, "硬件不存在");
        }

        order.setMoney(hard.getMoney());
        //获取硬件信息
        int basePacketId = 1;
        Hardware hardware = packet==null?hardwareMapper.selectById(basePacketId):hardwareMapper.selectById(packet.getHardwareId());

        OrderTask baseTask = new OrderTask(lifeTime,order){
            @Override
            public synchronized void recall() {
                super.recall();
                switch (order.getState()){
                    case "支付成功":
                        System.out.println("test to create");
                        String containerId = containerService.createContainer(addContainer, userId, hardware);
                        if (containerId != null) {
                            order.setContainerId(containerId);
                            order.setState("创建成功!");
                        } else {
                            order.setState("创建容器失败!");
                        }
                        break;
                    case "未支付":
                        log.info("创建订单失败！");
                        order.setState("创建失败，未支付");
                }
                orderMapper.updateById(order);
                death();
            }
            @Override
            public synchronized void run() {
                super.run();
                System.out.println("daled:"+getTime());
            }
        };
        taskThreadPool.addOrderTask(baseTask,order);

        return order;
    }

    @Override
    public String createQrCode(Order order) {
        return null;
    }

    @Override
    public boolean paied(Order order) {
        OrderTask orderTask = (OrderTask) getDTaskByOrderId(order.getId());
        if(orderTask==null||orderTask.getStatus()!= TaskStatus.RUNNING) {
            return false;
        }
        setPayedFromTask(orderTask);
        return true;
    }

    /**
     *  设置订单状态为支付成功
     * @param orderTask
     */
    private void setPayedFromTask(OrderTask orderTask){
        orderTask.setOrderState("支付成功");
    }

    /**
     * 通过orderId获取对应的Task
     * @param orderId
     * @return
     */
    public DTask getDTaskByOrderId(int orderId){
        return taskThreadPool.getTaskFromOrder(orderId);
    }

    @Override
    public List<Order> getAllOrderByUser(Long userId) {
        List<Order> orders = orderMapper.selectList(new QueryWrapper<Order>().eq("user_id",userId));
        return orders;
    }
}
