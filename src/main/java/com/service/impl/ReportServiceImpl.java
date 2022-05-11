package com.service.impl;

import com.domain.entity.Account;
import com.domain.entity.Report;
import com.repository.DiaryRepository;
import com.repository.ReportRepository;
import com.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final DiaryRepository diaryRepository;

    @Override
    public void insertReport(Account account, Long diaryId) {
        Report report = Report.builder()
                .account(account)
                .diary(diaryRepository.findById(diaryId).get())
                .build();
        reportRepository.save(report);
    }
}
