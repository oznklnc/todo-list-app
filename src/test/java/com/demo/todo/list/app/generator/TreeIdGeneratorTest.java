package com.demo.todo.list.app.generator;

import com.demo.todo.list.app.base.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

class TreeIdGeneratorTest extends UnitTest {

    @InjectMocks
    TreeIdGenerator treeIdGenerator;

    @Test
    void should_generate_tree_id() {
        //when
        String treeId = treeIdGenerator.generateTreeId();

        //then
        assertThat(treeId).isNotNull();
        assertThat(treeId).isUpperCase();
    }

}