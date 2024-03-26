package com.evolution.types.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.parser.NodeParser;

import java.util.List;
import java.util.stream.Collectors;


/**
 * 树工具类
 *
 * @author liuyi
 */
public class TreeUtil {

    /**
     * 树构建
     *
     * @param <T>            转换的实体 为数据源里的对象类型
     * @param <E>            ID类型
     * @param list           源数据集合
     * @param parentId       最顶层父id值 一般为 0 之类
     * @param treeNodeConfig 配置
     * @param nodeParser     转换器
     * @return List
     */
    public static <T, E> List<Tree<E>> build(List<Long> menuIdList, List<T> list, E parentId, TreeNodeConfig treeNodeConfig, NodeParser<T, E> nodeParser) {
        final List<Tree<E>> treeList = CollUtil.newArrayList();
        Tree<E> tree;
        for (T obj : list) {
            tree = new Tree<>(treeNodeConfig);
            nodeParser.parse(obj, tree);
            treeList.add(tree);
        }

        List<Tree<E>> finalTreeList = CollUtil.newArrayList();
        for (Tree<E> node : treeList) {
            if (parentId.equals(node.getParentId())) {
                finalTreeList.add(node);
                innerBuild(treeList, node, 0, treeNodeConfig.getDeep());
            }
            if (CollUtil.isEmpty(node.getChildren())) {
                node.put("checked", false);
                if (CollUtil.isNotEmpty(menuIdList)) {
                    Long menuId = Convert.toLong(node.getId());
                    if (menuIdList.contains(menuId)) {
                        node.put("checked", true);
                    }
                }
            }
        }
        // 内存每层已经排过了 这是最外层排序
        finalTreeList = finalTreeList.stream().sorted().collect(Collectors.toList());
        return finalTreeList;
    }

    /**
     * 递归处理
     *
     * @param treeNodes  数据集合
     * @param parentNode 当前节点
     * @param deep       已递归深度
     * @param maxDeep    最大递归深度 可能为null即不限制
     */
    private static <T> void innerBuild(List<Tree<T>> treeNodes, Tree<T> parentNode, int deep, Integer maxDeep) {

        if (CollUtil.isEmpty(treeNodes)) {
            return;
        }
        //maxDeep 可能为空
        if (maxDeep != null && deep >= maxDeep) {
            return;
        }
        // 每层排序 TreeNodeMap 实现了Comparable接口
        treeNodes = treeNodes.stream().sorted().collect(Collectors.toList());
        for (Tree<T> childNode : treeNodes) {
            if (parentNode.getId().equals(childNode.getParentId())) {
                List<Tree<T>> children = parentNode.getChildren();
                if (children == null) {
                    children = CollUtil.newArrayList();
                    parentNode.setChildren(children);
                }
                children.add(childNode);
                childNode.setParent(parentNode);
                innerBuild(treeNodes, childNode, deep + 1, maxDeep);
            }
        }
    }


}
