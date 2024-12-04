package com.demo.todo.list.app.controller;

import com.demo.todo.list.app.model.request.TodoRequest;
import com.demo.todo.list.app.model.response.ApiErrorResponse;
import com.demo.todo.list.app.model.response.TodoResponse;
import com.demo.todo.list.app.model.response.TodoWrapperResponse;
import com.demo.todo.list.app.service.business.TodoBusinessService;
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
@Tag(name = "Todo Controller", description = "This controller is responsible for todo create, update, delete and get todo information")
@RequestMapping("/api/v1/")
public class TodoController {

    private final TodoBusinessService todoBusinessService;

    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add Todo Root Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoResponse.class))
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
    @PostMapping("/todo")
    public ResponseEntity<TodoResponse> createTodo(@RequestBody @Valid TodoRequest todoRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(todoBusinessService.addTodo(todoRequest));
    }





    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Add Sub Todo Item To Parent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoResponse.class))
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
    @PostMapping("/todo/{parentId}")
    public ResponseEntity<TodoResponse> createSubTodo(@PathVariable("parentId") String parentId,
                                                      @RequestBody @Valid TodoRequest todoRequest) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(todoBusinessService.addSubTodo(todoRequest, parentId));
    }




    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Get Todo Item By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoResponse.class))
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
    @GetMapping("/todo/{id}")
    public ResponseEntity<TodoResponse> getTodoById(@PathVariable("id") String id) {
        return ResponseEntity.ok(todoBusinessService.getTodoById(id));
    }




    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Query Todo Items By Project Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoWrapperResponse.class))
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
    @GetMapping("/todo")
    public ResponseEntity<TodoWrapperResponse> getTodos(@RequestParam(value = "projectId") String projectId) {
        return ResponseEntity.ok(todoBusinessService.getTodos(projectId));
    }



    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete Todos By Parent Id")
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
    @DeleteMapping("/todo/{id}")
    public ResponseEntity<Void> deleteTodoById(@PathVariable("id") String id) {
        todoBusinessService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }



    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update Todo Item By Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content = {
                    @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TodoResponse.class))
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
    @PutMapping("/todo/{id}")
    public ResponseEntity<TodoResponse> updateTodoById(@PathVariable("id") String id,
                                                       @RequestBody @Valid TodoRequest todoRequest) {
        return ResponseEntity.ok(todoBusinessService.updateTodoById(id, todoRequest));
    }




    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Complete Todo Item By Parent Id")
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
    @PutMapping("/todo/{id}/complete")
    public ResponseEntity<Void> updateTodoByIdAsCompleted(@PathVariable("id") String id) {
        todoBusinessService.updateAsCompleted(id);
        return ResponseEntity
                .noContent()
                .build();
    }

}
