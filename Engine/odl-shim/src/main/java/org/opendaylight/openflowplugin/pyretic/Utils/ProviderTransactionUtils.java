package org.opendaylight.openflowplugin.pyretic.Utils;

import com.google.common.base.Optional;
import java.util.concurrent.ExecutionException;
import org.opendaylight.controller.md.sal.binding.api.ReadTransaction;
import org.opendaylight.controller.md.sal.common.api.data.LogicalDatastoreType;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public final class ProviderTransactionUtils {
	private static final Logger LOG = LoggerFactory.getLogger(ProviderTransactionUtils.class);
	private ProviderTransactionUtils() {
		throw new AssertionError("TestProviderTransactionUtil was not meant to be instantiated.");
	}
	public static <T extends DataObject> T getDataObject(ReadTransaction readOnlyTransaction, InstanceIdentifier<T> identifier) {
		Optional<T> optionalData = null;
		try {
			optionalData = readOnlyTransaction.read(LogicalDatastoreType.OPERATIONAL, identifier).get();
			if (optionalData.isPresent()) {
				return optionalData.get();
			}
		} catch (ExecutionException | InterruptedException e) {
			LOG.error("Read transaction for identifier {} failed.", identifier, e);
		}
		return null;
	}
}