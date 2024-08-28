package com.viaas.docker.connection;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Statistics;
import com.viaas.docker.entity.vo.Dashboard;
import lombok.Data;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * 自定义dashboardResult拦截器
 */
@Data
public class DashboardResultCallback extends ResultCallback.Adapter<Statistics> {

    private List<Dashboard> dashboards = new Vector();
    private String containerId;

    @Override
    public void onStart(Closeable closeable) {
        super.onStart(closeable);
    }

    @Override
    public void onNext(Statistics object) {
        super.onNext(object);
        Dashboard dashboard = new Dashboard();
        if(object.getCpuStats().getSystemCpuUsage()!=null&&object.getPreCpuStats().getSystemCpuUsage()!=null) {
            double cpuDelta = object.getCpuStats().getCpuUsage().getTotalUsage() - object.getPreCpuStats().getCpuUsage().getTotalUsage();
            double cpuSysDetal = object.getCpuStats().getSystemCpuUsage() - object.getPreCpuStats().getSystemCpuUsage();
            double cpuPortion = cpuDelta / cpuSysDetal * object.getCpuStats().getOnlineCpus() * 100f;
            dashboard.setCpuPortion((float) cpuPortion);
        }
        if(object.getMemoryStats().getUsage()!=null){
//                    double usedMemory = object.getMemoryStats().getUsage()-object.getMemoryStats().getStats().getCache();
            double usedMemory = object.getMemoryStats().getUsage();
            double availableMemory = object.getMemoryStats().getLimit();
            double memoryPortion = usedMemory/availableMemory*100f;
            dashboard.setMemoryPortion(memoryPortion);
        }
        if(dashboard.getCpuPortion()!=-1&&dashboard.getMemoryPortion()!=-1){
            dashboards.add(dashboard);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        super.onError(throwable);
    }

    @Override
    public void onComplete() {
        super.onComplete();
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
