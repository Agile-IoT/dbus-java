/*
   D-Bus Java Bindings
   Copyright (c) 2005-2006 Matthew Johnson

   This program is free software; you can redistribute it and/or modify it
   under the terms of either the GNU General Public License Version 2 or the
   Academic Free Licence Version 2.1.

   Full licence texts are included in the COPYING file with this program.
*/
package org.freedesktop.dbus;

import java.util.Vector;

public class MethodReturn extends Message
{
   MethodReturn() { }
   public MethodReturn(String dest, long replyserial, String sig, Object... args) 
   {
      super(Message.Endian.BIG, Message.MessageType.METHOD_RETURN, (byte) 0);

      headers.put(Message.HeaderField.DESTINATION,dest);
      headers.put(Message.HeaderField.REPLY_SERIAL,replyserial);

      Vector<Object> hargs = new Vector<Object>();
      hargs.add(new Object[] { Message.HeaderField.DESTINATION, new Object[] { ArgumentType.STRING_STRING, dest } });
      hargs.add(new Object[] { Message.HeaderField.REPLY_SERIAL, new Object[] { ArgumentType.UINT32_STRING, replyserial } });

      if (null != sig) {
         hargs.add(new Object[] { Message.HeaderField.SIGNATURE, new Object[] { ArgumentType.SIGNATURE_STRING, sig } });
         headers.put(Message.HeaderField.SIGNATURE,sig);
         this.args = args;
      }

      byte[] blen = new byte[4];
      appendBytes(blen);
      append("ua(yv)", ++serial, hargs.toArray());
      pad((byte)8);

      long c = bytecounter;
      if (null != sig) append(sig, args);
      marshallint(bytecounter-c, blen, 0, 4);
   }
   Call call;
   public Call getCall() { return call; }
   protected void setCall(Call call) { this.call = call; this.replyserial = call.getSerial(); this.destination = call.getSource();}
}
