package com.whiskas68.cloud.controller;


import com.whiskas68.cloud.util.KafkaManage;
import org.apache.kafka.clients.admin.CreatePartitionsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaManageController {

    @Autowired
    private KafkaManage kafkaManage;

    @PostMapping(value = "/kafka/api/increase")
    public CreatePartitionsResult increasePartition(@RequestParam("topic") String topic, @RequestParam("num") int num) throws Exception{
        return kafkaManage.increasePartitions(topic,num);
    }

    @RequestMapping(value="/kafka", method = RequestMethod.GET)
    public String kafkaHello(){
        return "Hello";
    }

    @PostMapping("/kafka/hello")
    public String hello(@RequestParam("name") String name,
                        @RequestParam("age") Integer age) {
        return "name：" + name + "\nage：" + age;
    }
}
