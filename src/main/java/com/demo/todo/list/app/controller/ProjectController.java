package com.demo.todo.list.app.controller;

import com.demo.todo.list.app.model.request.ProjectRequest;
import com.demo.todo.list.app.model.response.ApiErrorResponse;
import com.demo.todo.list.app.model.response.ProjectResponse;
import com.demo.todo.list.app.model.response.ProjectWrapperResponse;
import com.demo.todo.list.app.service.business.ProjectBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Project Controller", description = "This controller is responsible for project (like todo list) create, update, delete and get project information")
@RequestMapping("/api/v1/")
public class ProjectController {

    private final ProjectBusinessService projectBusinessService;


    @Operation(summary = "Create new project")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PostMapping("/project")
    public ResponseEntity<ProjectResponse> createProject(@RequestBody @Valid ProjectRequest projectRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(projectBusinessService.createProject(projectRequest));
    }


    @Operation(summary = "Get project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<ProjectResponse> getProjectById(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok(projectBusinessService.getProjectById(projectId));
    }

    @Operation(summary = "Update project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @PutMapping("/project/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable("projectId") String projectId,
                                                         @RequestBody @Valid ProjectRequest projectRequest) {
        return ResponseEntity.ok(projectBusinessService.updateProject(projectId, projectRequest));
    }

    @Operation(summary = "Delete project by project id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteProjectById(@PathVariable("projectId") String projectId) {
        projectBusinessService.deleteProjectById(projectId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all projects")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProjectWrapperResponse.class))
            }),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiErrorResponse.class))
            })
    })
    @SecurityRequirement(name = "Bearer Authentication")
    @GetMapping("/project/all")
    public ResponseEntity<ProjectWrapperResponse> getAllProjects() {
        return ResponseEntity.ok(projectBusinessService.getAllProjects());
    }
}
