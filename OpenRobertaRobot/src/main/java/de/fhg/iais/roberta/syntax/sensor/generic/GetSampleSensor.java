package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robSensors_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType} and port. See enum {@link SensorType} for all possible type of sensors.<br>
 */
@NepoBasic(containerType = "SENSOR_GET_SAMPLE", category = "SENSOR", blocklyNames = {"sim_getSample", "robSensors_getSample_ardu", "mbedsensors_getsample", "robSensors_getSample"})
public final class GetSampleSensor<V> extends Sensor<V> {
    public final Sensor<V> sensor;
    public final String sensorPort;
    public final String slot;
    public final String sensorTypeAndMode;
    public final Mutation mutation;
    public final List<Hide> hide;

    @SuppressWarnings("unchecked")
    public GetSampleSensor(
        String sensorTypeAndMode,
        String port,
        String slot,
        Mutation mutation,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        BlocklyDropdownFactory factory) //
    {
        super(properties, comment);
        Assert.notNull(sensorTypeAndMode);
        Assert.notNull(port);
        this.sensorPort = port;
        this.slot = slot;
        this.sensorTypeAndMode = sensorTypeAndMode;
        this.mutation = mutation;
        this.hide = hide;
        this.sensor = (Sensor<V>) factory.createSensor(sensorTypeAndMode, port, slot, mutation, properties, comment);
        setReadOnly();
    }

    public Sensor<V> getSensor() {
        return this.sensor;
    }

    public String getSensorPort() {
        return this.sensorPort;
    }

    public String getSlot() {
        return this.slot;
    }

    public List<Hide> getHide() {
        return this.hide;
    }

    /**
     * @return type of the sensor who will get the sample
     */
    public String getSensorTypeAndMode() {
        return this.sensorTypeAndMode;
    }

    public Mutation getMutation() {
        return this.mutation;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [" + this.sensor + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        String mutationInput = block.getMutation().getInput();
        String modeName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORTYPE, mutationInput);
        String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT);
        String robotGroup = helper.getRobotFactory().getGroup();
        boolean calliopeOrMicrobit = "calliope".equals(robotGroup) || "microbit".equals(robotGroup);
        boolean gyroOrAcc = mutationInput.equals("ACCELEROMETER_VALUE") || mutationInput.equals("GYRO_ANGLE");
        String slotName;
        if ( calliopeOrMicrobit && gyroOrAcc ) {
            slotName = Jaxb2Ast.extractNonEmptyField(fields, BlocklyConstants.SLOT, BlocklyConstants.X);
        } else {
            slotName = Jaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);
        }
        return new GetSampleSensor(modeName, portName, slotName, block.getMutation(), block.getHide(), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block), helper.getDropdownFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this.sensor, jaxbDestination);
        if ( this.mutation != null ) {
            jaxbDestination.setMutation(mutation);
        }
        if ( this.hide != null && this.hide.size() > 0 ) {
            jaxbDestination.getHide().addAll(hide);
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, this.sensorTypeAndMode);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.sensorPort);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, this.slot);

        return jaxbDestination;
    }

}
