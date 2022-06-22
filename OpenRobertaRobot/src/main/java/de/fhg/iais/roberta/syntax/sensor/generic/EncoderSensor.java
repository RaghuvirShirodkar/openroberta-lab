package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName = "ENCODER_DISTANCE", sensor = "ENCODER", mode = "DISTANCE"), @NepoSampleValue(blocklyFieldName = "ENCODER_DEGREE", sensor = "ENCODER", mode = "DEGREE"), @NepoSampleValue(blocklyFieldName = "ENCODER_ROTATION", sensor = "ENCODER", mode = "ROTATION")}, containerType = "ENCODER_SENSING", category = "SENSOR", blocklyNames = {"robSensors_encoder_reset", "robSensors_encoder_getSample"})
public final class EncoderSensor<V> extends ExternalSensor<V> {

    public EncoderSensor(BlocklyBlockProperties properties, BlocklyComment comment, SensorMetaDataBean sensorMetaDataBean) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorData;
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_ENCODER_RESET) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorData =
                new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return new EncoderSensor<V>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorData);
        }
        sensorData = extractPortAndModeAndSlot(block, helper);
        return new EncoderSensor<V>(Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), sensorData);
    }

    @Override
    public Block astToBlock() {
        if ( getMode().toString().equals("RESET") ) {
            Block jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            String fieldValue = getUserDefinedPort().toString();
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            return jaxbDestination;
        } else {
            return super.astToBlock();
        }

    }
}
