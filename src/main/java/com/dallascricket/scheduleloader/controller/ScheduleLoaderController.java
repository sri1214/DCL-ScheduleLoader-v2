package com.dallascricket.scheduleloader.controller;

import com.dallascricket.scheduleloader.excel.model.MatchData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ScheduleLoaderController {
    private final static Logger logger = LogManager.getLogger(ScheduleLoaderController.class);

    public void loadTapeBall() throws Exception {
        logger.info("\n\nProcessing TapeBall Schedule...");
        try {
            List<MatchData> matchDataList = scheduleLoader.readTapeBallSheet();
            processMatchData(matchDataList, new TapeBallAdapter(), tapeDAOManager);
        } catch (Exception e) {
            logger.error("Tapeball schedule upload failed\n", e);
            throw e;
        }
        logger.info("Tapeball schedule loaded!!!\n\n");

    }
}
