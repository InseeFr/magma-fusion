package fr.insee.rmes.magmafusion.api.testcontainers.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.Container.ExecResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GraphDBContainerTest {

    private GraphDBContainer container;

    @BeforeEach
    void setUp() {
        container = spy(new GraphDBContainer("ontotext/graphdb:10.8.8"));
    }

    @Test
    void constructor_shouldExposePort7200() {
        var exposedPorts = container.getExposedPorts();
        assertTrue(exposedPorts.contains(7200));
    }

    @Test
    void withInitFolder_shouldReturnSameInstance() {
        GraphDBContainer result = container.withInitFolder("/testcontainers");
        assertSame(container, result);
    }

    @Test
    void withRepository_shouldSucceedOnHttp201() throws IOException, InterruptedException {
        ExecResult execResult = mock(ExecResult.class);
        when(execResult.getStdout()).thenReturn("some output\nHTTP_STATUS:201");

        doNothing().when(container).copyFileToContainer(any(), anyString());
        doReturn(execResult).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        GraphDBContainer result = container.withRepository("config.ttl");

        assertSame(container, result);
        verify(container).copyFileToContainer(any(), eq("/docker-entrypoint-initdb/config.ttl"));
    }

    @Test
    void withRepository_shouldSucceedOnHttp409() throws IOException, InterruptedException {
        ExecResult execResult = mock(ExecResult.class);
        when(execResult.getStdout()).thenReturn("already exists\nHTTP_STATUS:409");

        doNothing().when(container).copyFileToContainer(any(), anyString());
        doReturn(execResult).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        assertDoesNotThrow(() -> container.withRepository("config.ttl"));
    }

    @Test
    void withRepository_shouldThrowOnUnexpectedHttpStatus() throws IOException, InterruptedException {
        ExecResult execResult = mock(ExecResult.class);
        when(execResult.getStdout()).thenReturn("error\nHTTP_STATUS:500");

        doNothing().when(container).copyFileToContainer(any(), anyString());
        doReturn(execResult).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        AssertionError error = assertThrows(AssertionError.class, () -> container.withRepository("config.ttl"));
        assertTrue(error.getMessage().contains("500"));
    }

    @Test
    void withRepository_shouldThrowOnIOException() throws IOException, InterruptedException {
        doNothing().when(container).copyFileToContainer(any(), anyString());
        doThrow(new IOException("connection refused")).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        AssertionError error = assertThrows(AssertionError.class, () -> container.withRepository("config.ttl"));
        assertEquals("The TTL file was not loaded", error.getMessage());
        assertInstanceOf(IOException.class, error.getCause());
    }

    @Test
    void withTrigFiles_shouldSucceedOnHttp204() throws IOException, InterruptedException {
        ExecResult execResult = mock(ExecResult.class);
        when(execResult.getStdout()).thenReturn("204");

        doNothing().when(container).copyFileToContainer(any(), anyString());
        doReturn(execResult).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        GraphDBContainer result = container.withTrigFiles("statementsGeoTest.trig");

        assertSame(container, result);
        verify(container).copyFileToContainer(any(), eq("/docker-entrypoint-initdb/statementsGeoTest.trig"));
    }

    @Test
    void withTrigFiles_shouldThrowOnUnexpectedHttpStatus() throws IOException, InterruptedException {
        ExecResult execResult = mock(ExecResult.class);
        when(execResult.getStdout()).thenReturn("500");

        doNothing().when(container).copyFileToContainer(any(), anyString());
        doReturn(execResult).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        AssertionError error = assertThrows(AssertionError.class, () -> container.withTrigFiles("statementsGeoTest.trig"));
        assertTrue(error.getMessage().contains("500"));
    }

    @Test
    void withTrigFiles_shouldThrowOnIOException() throws IOException, InterruptedException {
        doNothing().when(container).copyFileToContainer(any(), anyString());
        doThrow(new IOException("timeout")).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        AssertionError error = assertThrows(AssertionError.class, () -> container.withTrigFiles("statementsGeoTest.trig"));
        assertEquals("The Trig file was not loaded", error.getMessage());
        assertInstanceOf(IOException.class, error.getCause());
    }

    @Test
    void withTrigFiles_shouldThrowOnInterruptedException() throws IOException, InterruptedException {
        doNothing().when(container).copyFileToContainer(any(), anyString());
        doThrow(new InterruptedException("interrupted")).when(container).execInContainer(any(String[].class));

        container.withInitFolder("/testcontainers");
        AssertionError error = assertThrows(AssertionError.class, () -> container.withTrigFiles("statementsGeoTest.trig"));
        assertEquals("The Trig file was not loaded", error.getMessage());
        assertInstanceOf(InterruptedException.class, error.getCause());
    }

    @Test
    void start_shouldNotRestartIfAlreadyRunning() {
        doReturn(true).when(container).isRunning();

        container.start();

        verify(container, never()).withInitFolder(anyString());
    }

    @Test
    void dockerEntrypointInitdb_shouldHaveCorrectValue() {
        assertEquals("/docker-entrypoint-initdb", GraphDBContainer.DOCKER_ENTRYPOINT_INITDB);
    }
}