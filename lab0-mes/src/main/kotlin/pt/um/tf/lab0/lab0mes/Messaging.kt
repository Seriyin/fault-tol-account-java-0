package pt.um.tf.lab0.lab0mes

import io.atomix.catalyst.buffer.BufferInput
import io.atomix.catalyst.buffer.BufferOutput
import io.atomix.catalyst.serializer.CatalystSerializable
import io.atomix.catalyst.serializer.Serializer

data class Message(var op : Int, var mov : Int) : CatalystSerializable  {
    override fun writeObject(buffer: BufferOutput<*>?, serializer: Serializer?) {
        buffer?.writeInt(op)?.writeInt(mov);
    }

    override fun readObject(buffer: BufferInput<*>?, serializer: Serializer?) {
        op = buffer!!.readInt();
        mov = buffer.readInt();
    }
}

data class Reply(var op : Int, var res : Boolean, var balance : Int) : CatalystSerializable {
    override fun readObject(buffer: BufferInput<*>?, serializer: Serializer?) {
        op = buffer!!.readInt()
        res = buffer.readBoolean()
        balance = buffer.readInt()
    }

    override fun writeObject(buffer: BufferOutput<*>?, serializer: Serializer?) {
        buffer?.writeInt(op)?.writeBoolean(res)?.writeInt(balance)
    }

}