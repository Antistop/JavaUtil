package com.hanhan.javautil.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Profile {
    private static ThreadLocal<Integer> index = ThreadLocal.withInitial(() -> -1);
    private static ThreadLocal<Integer> sequence = ThreadLocal.withInitial(() -> -1);
    private static ThreadLocal<Stack<Node>> profile = ThreadLocal.withInitial(() -> new Stack<>());
    private static ThreadLocal<StringBuilder> log = ThreadLocal.withInitial(() -> new StringBuilder("\n"));
    private static ThreadLocal<Map<String, Long>> costMap = ThreadLocal.withInitial(() -> new HashMap<>());

    public static long getCost(String path) {
        Long cost = costMap.get().get(path);
        return cost == null ? 0 : cost;
    }

    private static class Node implements Comparable<Node> {
        private String name;
        private long time;
        private int index;
        private int sequence;

        @Override
        public int compareTo(Node o) {
            return this.sequence - o.sequence;
        }
    }

    public static void enter(String name) {
        Node node = new Node();
        node.name = name;
        node.time = System.currentTimeMillis();
        int idx = index.get() + 1;
        int seq = sequence.get() + 1;
        node.index = idx;
        node.sequence = seq;
        index.set(idx);
        sequence.set(seq);
        profile.get().push(node);
        for (int i = 0; i < node.index; i++) {
            log.get().append("    ");
        }
        log.get().append(node.name).append("--->begin:").append(node.time).append("\n");
    }

    public static void release(String name) {
        Node node = new Node();
        node.name = name;
        node.time = System.currentTimeMillis();
        int idx = index.get();
        node.index = idx;
        index.set(idx - 1);
        int seq = sequence.get() + 1;
        node.sequence = seq;
        sequence.set(seq);
        StringBuilder sb = log.get();
        if (profile.get().isEmpty()) {
            sb.append(node.name).append("--->end:").append(node.time).append(" not find bigin").append("\n");
            return;
        }
        Node begin = profile.get().pop();
        for (int i = 0; i < node.index; i++) {
            sb.append("    ");
        }
        if (begin.index != node.index) {
            sb.append(node.name).append("--->end:").append(node.time).append(" begin not match").append("\n");
            return;
        }
        long cost = node.time - begin.time;
        sb.append(node.name).append("--->").append("end:").append(node.time).append(",cost:").append(cost).append("\n");
        costMap.get().put(node.name, cost);
    }

    public static void reset() {
        profile.remove();
        index.remove();
        sequence.remove();
        log.remove();
        costMap.remove();
    }


    public static String end() {
        String profile = log.get().toString();
        reset();
        return profile;
    }

    public static void main(String[] args) throws InterruptedException {
        Profile.enter("OrderService.QueryOrder");
        Thread.sleep(19);
        Profile.enter("OrderService.QueryOrderCache");
        Thread.sleep(209);
        Profile.release("OrderService.QueryOrderCache");

        Profile.enter("OrderService.QueryOrderDB");
        Thread.sleep(402);
        Profile.release("OrderService.QueryOrderDB");
        Profile.enter("OrderService.PutOrderCache");
        Thread.sleep(53);
        Profile.release("OrderService.PutOrderCache");

        Profile.enter("OrderService.QueryItem");
        Thread.sleep(19);
        Profile.enter("OrderService.QueryItemCache");
        Thread.sleep(209);
        Profile.release("OrderService.QueryItemCache");

        Profile.enter("OrderService.QueryItemDB");
        Thread.sleep(402);
        Profile.release("OrderService.QueryItemDB");
        Profile.enter("OrderService.PutItemCache");
        Thread.sleep(53);
        Profile.release("OrderService.PutItemCache");

        Profile.release("OrderService.QueryItem");
        Profile.release("OrderService.QueryOrder");
        System.out.println(Profile.end());
    }
}
