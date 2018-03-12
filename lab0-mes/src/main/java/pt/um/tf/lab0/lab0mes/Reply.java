package pt.um.tf.lab0.lab0mes;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

/**
 * A simple Reply POJO for RMI.
 */
public class Reply implements CatalystSerializable {
    private byte op;
    private boolean denied;
    private int bal;

    Reply() {}

    public Reply(byte op, boolean denied, int bal) {
        this.op = op;
        this.denied = denied;
        this.bal = bal;
    }

    public byte getOp() {
        return op;
    }

    public boolean isDenied() {
        return denied;
    }

    public int getBalance() {
        return bal;
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        buffer.writeByte(op);
        buffer.writeBoolean(denied);
        buffer.writeInt(bal);
    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        op = (byte) buffer.readByte();
        denied = buffer.readBoolean();
        bal = buffer.readInt();
    }
}
