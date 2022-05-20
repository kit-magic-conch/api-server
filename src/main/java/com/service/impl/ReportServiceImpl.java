package com.service.impl;

import com.domain.entity.Report;
import com.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    @Override
    public void insertReport(Long accountId, Long diaryId) {
        Report report = Report.builder()
                .account(accountRepository.findById(accountId).get())
                .diary(diaryRepository.findById(diaryId).get())
                .build();
        reportRepository.save(report);
    }
}
