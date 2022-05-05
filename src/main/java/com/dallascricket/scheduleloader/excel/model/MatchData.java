package com.dallascricket.scheduleloader.excel.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class MatchData {
    private String week;
    private Date date;
    private String division;
    private double tournamentId;
    private String team1;
    private String team2;
    private String venue;
    private String umpire1;
    private String umpire2;
    private String day;
    private String time;
}
