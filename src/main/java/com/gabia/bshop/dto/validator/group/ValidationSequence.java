package com.gabia.bshop.dto.validator.group;

import com.gabia.bshop.dto.validator.group.FileValidationGroups.NotEmptyFileListGroup;
import com.gabia.bshop.dto.validator.group.FileValidationGroups.NotSuppoertedFileGroup;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyFileListGroup.class, NotSuppoertedFileGroup.class})
public interface ValidationSequence {
}
