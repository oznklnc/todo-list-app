package com.demo.todo.list.app.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectWrapperResponse {

    private Integer size;
    private List<ProjectResponse> projects;

}
