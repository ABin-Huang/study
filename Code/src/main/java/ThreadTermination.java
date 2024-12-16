/**
 * @author abin
 * @date 2024/12/14 15:12
 * @description
 * 两阶段终止模式：发送终止指令+响应终止指令(interrupt()方法和终止标识变量)。
 * 优雅的线程终止
 *  1.自定义terminated标志位 推荐
 *  2.Thread.currentThread().interrupt();这种方式不可控，没有办法保证第三方类库正确处理了线程的中断异常，
 *
 * 线程池优雅终止
 *  1.shutdown，保守的关闭线程池的方法。线程池执行 shutdown() 后，就会拒绝接收新的任务，
 *      但是会等待线程池中正在执行的任务和已经进入阻塞队列的任务都执行完之后才最终关闭线程池。
 *  2.shutdownNow，会拒绝接收新的任务，同时还会中断线程池中正在执行的任务，已经进入阻塞队列的任务也被剥夺了执行的机会
 *      这些任务会作为方法的返回值返回。任务保存起来，后续以补偿的方式重新执行
 */

public class ThreadTermination {
    //线程终止标志位
    volatile boolean terminated = false;
    boolean started = false;
    //采集线程
    Thread rptThread;
    //启动采集功能
    synchronized void start(){
        //不允许同时启动多个采集线程
        if (started) {
            return;
        }
        started = true;
        terminated = false;
        rptThread = new Thread(()->{
            while (!terminated){
                //省略采集、回传实现
                report();
                //每隔两秒钟采集、回传一次数据
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e){
                    //重新设置线程中断状态，interrupt会将线程休眠状态转为runnable状态
                    //Thread.currentThread().interrupt();
                    System.out.println("线程被中断，重新设置中断状态");
                }
            }
            //执行到此处说明线程马上终止
            started = false;
        });
        rptThread.start();
    }

    private void report() {
        System.out.println("采集并回传数据");
    }

    //终止采集功能
    synchronized void stop(){
        //设置中断标志位
        terminated = true;
        //中断线程rptThread
        rptThread.interrupt();
    }
    
}
