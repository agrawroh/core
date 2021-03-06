package com.ukubuka.core.operations.transform;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.ukubuka.core.evaluator.UkubukaExpressionEvaluator;
import com.ukubuka.core.exception.PipelineException;
import com.ukubuka.core.exception.ReaderException;
import com.ukubuka.core.exception.TransformException;
import com.ukubuka.core.model.FileContents;
import com.ukubuka.core.model.FileRecord;
import com.ukubuka.core.model.SupportedSource;
import com.ukubuka.core.model.TransformOperation;
import com.ukubuka.core.model.UkubukaSchema;
import com.ukubuka.core.model.UkubukaSchema.Transform;
import com.ukubuka.core.model.UkubukaSchema.TransformOperations;
import com.ukubuka.core.model.UkubukaSchema.TransformOperationsType;
import com.ukubuka.core.reader.UkubukaReader;

/**
 * Ukubuka Transformer Test
 * 
 * @author agrawroh
 * @version v1.0
 */
public class UkubukaTransformerTest {

    /**************************** Dependency Mocks ***************************/
    @Mock
    private UkubukaReader reader;

    @Mock
    private UkubukaExpressionEvaluator expressionEvaluator;

    @InjectMocks
    private UkubukaTransformer ukubukaTransformer;

    /**************************** Initialize Mocks ***************************/
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /******************************** Test(s) ********************************/
    @Test
    public void test_performOperations_add_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.ADD);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("new java.util.Random().nextInt(100)");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(0xCafeBabe);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(3, fileHeader.size());
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test
    public void test_performOperations_add_withSrct_success()
            throws PipelineException, TransformException, ReaderException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.ADD);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("$RANDOM$.nextInt(100)");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(0xCafeBabe);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        Mockito.when(reader.readFileAsString(Mockito.any(SupportedSource.class),
                Mockito.anyString(), Mockito.anyString()))
                .thenReturn("$RANDOM$=new java.util.Random()");

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(3, fileHeader.size());
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test
    public void test_performOperations_new_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.NEW);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("new String('barfoo')");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(0xCafeBabe);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(3, fileHeader.size());
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test
    public void test_performOperations_new_idNoMatch_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.NEW);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("new String('barfoo')");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(0xCafeBabe);

        Transform transforms = new Transform();
        transforms.setId("foo-Y");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(0)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
    }

    @Test
    public void test_performOperations_delete_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.DELETE);
        transformOperation.setSource("foo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(1, fileHeader.size());
        assertFalse(fileHeader.contains("foo"));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_delete_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.DELETE);
        transformOperation.setSource("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test
    public void test_performOperations_remove_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.REMOVE);
        transformOperation.setSource("bar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(1, fileHeader.size());
        assertFalse(fileHeader.contains("bar"));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_remove_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.REMOVE);
        transformOperation.setSource("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test
    public void test_performOperations_rename_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.RENAME);
        transformOperation.setSource("foo");
        transformOperation.setTarget("foobar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(2, fileHeader.size());
        assertFalse(fileHeader.contains("foo"));
        assertTrue(fileHeader.contains("foobar"));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_rename_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.RENAME);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("barfoo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test
    public void test_performOperations_swap_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.SWAP);
        transformOperation.setSource("foo");
        transformOperation.setTarget("bar");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(2, fileHeader.size());
        assertEquals("bar", fileHeader.get(0));
        assertEquals("foo", fileHeader.get(1));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_swap_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.SWAP);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("barfoo");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test
    public void test_performOperations_move_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MOVE);
        transformOperation.setSource("bar");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(2, fileHeader.size());
        assertEquals("bar", fileHeader.get(0));
        assertEquals("foo", fileHeader.get(1));
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_move_failure()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MOVE);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test(expected = NullPointerException.class)
    public void test_performOperations_invalid_operation()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setSource("foo");
        transformOperation.setTarget("0");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test(expected = PipelineException.class)
    public void test_performOperations_unsupported_operation()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.NONE);
        transformOperation.setSource("foo");
        transformOperation.setTarget("new java.util.Random(100).nextInt(50)");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(Arrays.asList("bar", "foo"))),
                new FileRecord(new ArrayList<>(Arrays.asList("foo", "bar")))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));
        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);
    }

    @Test
    public void test_performOperations_sum_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.SUM);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("data[1]");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("2"))))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(2);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(new BigDecimal(4),
                dataFiles.get("foo-X").getAggregations().get("foobar"));
    }

    @Test
    public void test_performOperations_min_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MIN);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("data[1]");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(2);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(new BigDecimal(2),
                dataFiles.get("foo-X").getAggregations().get("foobar"));
    }

    @Test
    public void test_performOperations_max_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.MAX);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("data[1]");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(3);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertEquals(new BigDecimal(3),
                dataFiles.get("foo-X").getAggregations().get("foobar"));
    }

    @Test
    public void test_performOperations_avg_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.AVG);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("data[1]");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("3")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(new BigDecimal(3));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
        assertNotNull(dataFiles.get("foo-X").getAggregations().get("foobar"));
    }

    @Test
    public void test_performOperations_count_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.COUNT);
        transformOperation.setSource("foobar");
        transformOperation.setTarget("data[1]");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        List<TransformOperations> operationsList = new ArrayList<>(
                Arrays.asList(transformOperation));

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setColumn(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        assertEquals(new BigDecimal(2),
                dataFiles.get("foo-X").getAggregations().get("foobar"));

    }

    @Test
    public void test_performOperations_include_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.INCLUDE);
        transformOperation.setTarget("$DOUBLE$(data[1]) < 3d");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        TransformOperations operationsList = transformOperation;

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(true);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setRow(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
    }

    @Test
    public void test_performOperations_include_false()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.INCLUDE);
        transformOperation.setTarget("$DOUBLE$(data[1]) < 3d");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        TransformOperations operationsList = transformOperation;

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(false);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setRow(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
    }

    @Test
    public void test_performOperations_exclude_success()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.EXCLUDE);
        transformOperation.setTarget("$DOUBLE$(data[1]) < 3d");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        TransformOperations operationsList = transformOperation;

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(true);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setRow(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
    }

    @Test
    public void test_performOperations_exclude_false()
            throws PipelineException, TransformException {
        TransformOperations transformOperation = new TransformOperations();
        transformOperation.setType(TransformOperation.EXCLUDE);
        transformOperation.setTarget("$DOUBLE$(data[1]) < 3d");

        List<String> fileHeader = new ArrayList<>(Arrays.asList("foo", "bar"));
        List<FileRecord> fileRecords = new ArrayList<>(Arrays.asList(
                new FileRecord(new ArrayList<>(
                        Arrays.asList("bar", String.valueOf("2")))),
                new FileRecord(new ArrayList<>(
                        Arrays.asList("foo", String.valueOf("3"))))));
        TransformOperations operationsList = transformOperation;

        Mockito.when(
                expressionEvaluator.evaluate(Mockito.any(FileContents.class),
                        Mockito.any(FileRecord.class), Mockito.anyString()))
                .thenReturn(false);

        Transform transforms = new Transform();
        transforms.setId("foo-X");
        TransformOperationsType transformOperationsType = new TransformOperationsType();
        transformOperationsType.setRow(operationsList);
        transforms.setOperations(transformOperationsType);
        Map<String, FileContents> dataFiles = new HashMap<>();
        dataFiles.put("foo-X", new FileContents(fileHeader, fileRecords));

        UkubukaSchema ukubukaSchema = new UkubukaSchema();
        ukubukaSchema.setTransforms(Arrays.asList(transforms));

        ukubukaTransformer.performOperations(dataFiles, ukubukaSchema);

        Mockito.verify(expressionEvaluator, Mockito.times(2)).evaluate(
                Mockito.any(FileContents.class), Mockito.any(FileRecord.class),
                Mockito.anyString());
    }
}
