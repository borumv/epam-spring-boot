package com.example.workloadservice.mapstruct;

import com.example.workloadservice.dto.TrainerWorkloadSummaryDTO;
import com.example.workloadservice.dto.YearlyTrainingSummaryDTO;
import com.example.workloadservice.model.TrainerWorkloadSummary;
import com.example.workloadservice.model.YearlyTrainingSummary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TrainerWorkloadSummaryMapper {
    TrainerWorkloadSummaryMapper INSTANCE = Mappers.getMapper(TrainerWorkloadSummaryMapper.class);

    TrainerWorkloadSummaryDTO toDTO(TrainerWorkloadSummary summary);

    YearlyTrainingSummaryDTO toDTO(YearlyTrainingSummary yearlySummary);

    List<TrainerWorkloadSummaryDTO> toDTOList(List<TrainerWorkloadSummary> summaries);
}

