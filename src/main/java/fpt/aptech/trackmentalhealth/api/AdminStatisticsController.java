package fpt.aptech.trackmentalhealth.api;


import fpt.aptech.trackmentalhealth.service.admin.AdminStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/statistics")
@CrossOrigin("*")
public class AdminStatisticsController {

    //Thực hiện tính năng thống kê của admin (admin dashboard)
    @Autowired
    private AdminStatisticsService statisticsService;

    @GetMapping
    public Map<String, Long> getDashboardStatistics() {
        return statisticsService.getAllStatistics();
    }
}
