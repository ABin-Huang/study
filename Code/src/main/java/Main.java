/**
 * @author abin
 * @date 2024/12/14 15:10
 * @description
 */

public class Main {

    public static void main(String[] args) {
        // 创建 ThreadTermination 实例
        ThreadTermination termination = new ThreadTermination();

        // 启动采集功能
        termination.start();
        System.out.println("采集线程已启动");

        // 模拟运行一段时间后停止采集功能
        try {
            Thread.sleep(10000); // 模拟运行10秒
        } catch (InterruptedException e) {
            System.out.println("主线程在等待期间被中断");
            e.printStackTrace();
        }

        // 终止采集功能
        termination.stop();
        System.out.println("采集线程已终止");

        try {
            termination.rptThread.join();
        } catch (InterruptedException e) {
            System.out.println("主线程被中断");
            e.printStackTrace();
        }
    }
}
