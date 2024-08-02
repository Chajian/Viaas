package com.viaas.docker.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 目录树
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreeNode {
    /*当前目录或者文件名*/
    private String name;
    /*绝对路径*/
    private String absolutePath;
    /*
    标识符  文件类型
    / 目录
    * 可执行文件.
    | FIFO命名管道.
    @ 符号链接.
    = socket.
    */
    private String type;
    /*子节点*/
    private List<TreeNode> treeNodeList = new ArrayList<>();

    public void addNode(TreeNode node){
        treeNodeList.add(node);
    }
}
