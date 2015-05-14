package org.opendaylight.openflowplugin.pyretic.Utils;

import java.util.*;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.meter._case.MeterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.instruction.MeterCaseBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.flow.InstructionsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.MeterId;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.InstructionKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.types.rev131026.instruction.list.Instruction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.NodeKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.controller.md.sal.binding.api.ReadOnlyTransaction;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.Table;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.TableKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.Flow;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.tables.table.FlowKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.statistics.rev130819.FlowStatisticsData;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.Nodes;

/**
 * Created by Lucía Peñaranda Pardo
 */


public class StatsUtils {
	
	 private static final Logger LOG = LoggerFactory
			 .getLogger(StatsUtils.class);
	 private static DataBroker dataProviderService;
	
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
	
	/**
	 * Check out https://github.com/opendaylight/openflowplugin/blob/f478d4f38aac837545bf33c0d8745ce9cde3703c/test-provider/src/main/java/org/opendaylight/openflowplugin/test/OpenflowpluginStatsTestCommandProvider.java	
	 * @param 
	 */
	 public static void _flowStats() {
		 int flowCount = 0;
		 int flowStatsCount = 0;
		 List<Node> nodes = getNodes();
		 for (Iterator<Node> iterator = nodes.iterator(); iterator.hasNext();) {
			 NodeKey nodeKey = iterator.next().getKey();
			 InstanceIdentifier<FlowCapableNode> nodeRef = InstanceIdentifier
					 .create(Nodes.class).child(Node.class, nodeKey)
					 .augmentation(FlowCapableNode.class);
			 ReadOnlyTransaction readOnlyTransaction = dataProviderService
					 .newReadOnlyTransaction();
			 FlowCapableNode node = ProviderTransactionUtils.getDataObject(
					 readOnlyTransaction, nodeRef);
			 if (node != null) {
				 List<Table> tables = node.getTable();
				 for (Iterator<Table> iterator2 = tables.iterator(); iterator2.hasNext();) {
					 TableKey tableKey = iterator2.next().getKey();
					 InstanceIdentifier<Table> tableRef = InstanceIdentifier
							 .create(Nodes.class).child(Node.class, nodeKey)
							 .augmentation(FlowCapableNode.class).child(Table.class, tableKey);
					 Table table = ProviderTransactionUtils.getDataObject(
							 readOnlyTransaction, tableRef);
					 if (table != null) {
						 if (table.getFlow() != null) {
							 List<Flow> flows = table.getFlow();
							 for (Iterator<Flow> iterator3 = flows.iterator(); iterator3.hasNext();) {
								 flowCount++;
								 FlowKey flowKey = iterator3.next().getKey();
								 InstanceIdentifier<Flow> flowRef = InstanceIdentifier
										 .create(Nodes.class).child(Node.class, nodeKey)
										 .augmentation(FlowCapableNode.class).child(Table.class, tableKey)
										 .child(Flow.class, flowKey);	
								 Flow flow = ProviderTransactionUtils.getDataObject(
										 readOnlyTransaction, flowRef);
								 if (flow != null) {
									 FlowStatisticsData data = flow
											 .getAugmentation(FlowStatisticsData.class);
									 if (null != data) {
										 flowStatsCount++;
										 LOG.debug("--------------------------------------------");
									 }
								 }
							 }
						 }
					 }
				 }
			 }
		 	}
		 	if (flowCount == flowStatsCount) {
		 		LOG.debug("flowStats - Success");
		 		System.out.println("_flowStats() EXITO");
		 	} else {
		 		LOG.debug("flowStats - Failed");
		 		LOG
		 		.debug("System fetchs stats data in 50 seconds interval, so pls wait and try again.");
		 	}
	}

	 private static List<Node> getNodes() {
		 ReadOnlyTransaction readOnlyTransaction = dataProviderService
				 .newReadOnlyTransaction();
		 	InstanceIdentifier<Nodes> nodesID = InstanceIdentifier.create(Nodes.class);
		 	Nodes nodes = ProviderTransactionUtils.getDataObject(readOnlyTransaction,
		 			nodesID);
		 	if (nodes == null) {
		 		throw new RuntimeException("nodes are not found, pls add the node.");
		 	}
		 	return nodes.getNode();
	}

	
/*	public synchronized static TransmitPacketInput createFlowStatsRequest(final String dpid,  final String outPort){
		
		TransmitPacketInputBuilder tPackBuilder = new TransmitPacketInputBuilder();
		
		// We create a Node connector associated to the nodeId
		NodeRef ref = OutputUtils.createNodeRef(dpid);
		NodeConnectorRef nEgressConfRef = new NodeConnectorRef(OutputUtils.createNodeConnRef(dpid, outPort));

		// We create the action list of the packet (output packet to outPort)
		// 	Similar to how we create action lists in OutputUtils (packetOut)
		// The difference is where we send the packets (Uri changes)
		
		InstructionsBuilder req = new InstructionsBuilder();
    	req = StatsUtils.createMeterInstructions();
		
	/*	List<Action> actionList = new ArrayList<Action>();
		ActionBuilder ab = new ActionBuilder();
		OutputActionBuilder output = new OutputActionBuilder();
		output.setMaxLength(Integer.valueOf(0xffff));
		Uri value = new Uri(outPort);
		output.setOutputNodeConnector(value);
		ab.setAction(new OutputActionCaseBuilder().setOutputAction(output.build()).build());
		ab.setOrder(0);
		ab.setKey(new ActionKey(0));
		actionList.add(ab.build());//
				
		tPackBuilder.setConnectionCookie(null);
		//tPackBuilder.setAction(actionList);
		tPackBuilder.setPayload(eth.getRawPayload());
		tPackBuilder.setNode(ref);
		tPackBuilder.setEgress(nEgressConfRef);
		tPackBuilder.setBufferId(Long.valueOf(0xffffffffL));
		return tPackBuilder.build();
	}*/
	
	

	
}
