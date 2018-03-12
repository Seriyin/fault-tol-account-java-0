package pt.um.tf.lab0.lab0mes;

import io.atomix.catalyst.buffer.BufferInput;
import io.atomix.catalyst.buffer.BufferOutput;
import io.atomix.catalyst.serializer.CatalystSerializable;
import io.atomix.catalyst.serializer.Serializer;

/**
 * A simple message POJO.
 */
public class Message implements CatalystSerializable {
    private byte op;
    private int mov;

    Message() {}

    public Message(byte op, int mov) {
        this.op = op;
        this.mov = mov;
    }

    public byte getOp() {
        return op;
    }

    public int getMov() {
        return mov;
    }

    @Override
    public void writeObject(BufferOutput<?> buffer, Serializer serializer) {
        buffer.writeByte(op);
        buffer.writeInt(mov);
    }

    @Override
    public void readObject(BufferInput<?> buffer, Serializer serializer) {
        op = (byte) buffer.readByte();
        mov = buffer.readInt();
    }
}
