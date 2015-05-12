package org.opendaylight.openflowplugin.pyretic.Utils;

import java.util.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.meter._case.MeterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.MeterCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;

/**
 * Created by Lucía Peñaranda Pardo
 */


public class StatsUtils {
	
	/**
	* Check out https://github.com/opendaylight/openflowplugin/blob/f478d4f38aac837545bf33c0d8745ce9cde3703c/test-provider/src/main/java/org/opendaylight/openflowplugin/test/OpenflowpluginTestCommandProvider.java
	* @return
	*/
	public static InstructionsBuilder createMeterInstructions() {
		MeterBuilder aab = new MeterBuilder();
		aab.setMeterId(new MeterId(new Long(1)));
		InstructionBuilder ib = new InstructionBuilder();
		ib.setInstruction(new MeterCaseBuilder().setMeter(aab.build()).build());
		// Put our Instruction in a list of Instructions
		InstructionsBuilder isb = new InstructionsBuilder();
		List<Instruction> instructions = new ArrayList<Instruction>();
		ib.setKey(new InstructionKey(0));
		instructions.add(ib.build());
		isb.setInstruction(instructions);
		return isb;
	}

	
}
