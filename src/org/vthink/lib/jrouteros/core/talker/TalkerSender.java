/**
 * Copyright 2011 Virtual Think Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vthink.lib.jrouteros.core.talker;

import java.util.LinkedList;

import org.vthink.lib.jrouteros.core.Connector;
import org.vthink.lib.jrouteros.core.entity.Attribute;
import org.vthink.lib.jrouteros.core.util.SentenceUtil;

/**
 * This class is used for sending data from MikroTik RouterOS.
 * 
 * @author Lalu Erfandi Maula Yusnu
 */
public class TalkerSender {
	private boolean debug = false;
	private Connector connector;

	/**
	 * Constructor for this class
	 * 
	 * @param con {@link Connector}
	 */
	public TalkerSender(Connector con){
		this.connector = con;
	}

	/**
	 * This method used for sending data to MikroTik RouterOS
	 * 
	 * @param sentence {@link SentenceUtil}
	 * @return boolean
	 */
	public boolean send(SentenceUtil sentence){
		String command = this.createSentence(sentence);
		return this.connector.send(command);
	}


	/**
	 * This method used for wrapping command with clause where and 
	 * make it to the first position
	 * 
	 * @param sentence {@link SentenceUtil}
	 * @return SentenceUtil initialized object
	 */
	private SentenceUtil sentenceWrapper(SentenceUtil sentence){
		//command wrapper 
		LinkedList<Attribute> tmp = new LinkedList<Attribute>();
		for (Attribute attr : sentence.getBuildCommand()) {
			if (attr.getClause().contains("command")){
				tmp.addFirst(attr);
			} else {
				tmp.add(attr);
			}
		}

		//build up
		SentenceUtil outSentence = new SentenceUtil();
		for (Attribute attribute : tmp) {
			outSentence.add(attribute);
		}
		return outSentence;
	}

	/**
	 * Create valid query for send to MikroTik RouterOS
	 * @param queryObjectRecord QueryObjectRecord
	 * @return String
	 */
	private String createSentence(SentenceUtil sentence){
		SentenceUtil st = sentenceWrapper(sentence);
		String cmd="";
		StringBuilder sb = new StringBuilder();
		int i=0;
		for (Attribute qObject : st.getBuildCommand()){
			String clause	= qObject.getClause();
			String name 	= qObject.getName();
			String val  	= qObject.getValue();

			if (clause.contains("commandPrint")){
				sb.append(val);
				cmd="print";
			} else if (clause.contains("commandReguler")) {
				sb.append(val);
				cmd="reguler";
			} else {
				if (name.contains("proplist")||name.contains("tag")){
					sb.append("=.").append(name).append("=").append(val);
				}

				if (clause.contains("where")&&cmd.equals("print")){
					sb.append("?").append(name).append(val);
				}
				
				if (clause.contains("where")&&cmd.equals("reguler")){
					sb.append("=").append(name).append(val);
				}
				
				if (clause.equals("orWhere")||clause.equals("whereNot")||
						clause.equals("andWhere")||clause.equals("orWhereNot")||
						clause.equals("andWhereNot")){
					sb.append(name).append(val);
				}

				if (clause.equals("setAttribute")){
					sb.append("=").append(name).append("=").append(val);
				}
			}
			if (i!=st.getBuildCommand().size()-1) sb.append("\n");
			i++;
		}
		this.runDebugger(sb.toString());
		return sb.toString();
	}

	/**
	 * Run Debugger output with some formated Information
	 * @param debugString String
	 */
	private void runDebugger(String debugString){
		if (isDebug()){
			System.out.println("===================== Sender Debug ======================");
			System.out.println(debugString);
			System.out.println("===================== End Sender Debug ==================");
		}
	}

	/**
	 * Check if debugging mode is on
	 * @return boolean
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Set debugging to see what data is send to MikroTik RouterOS
	 * @param on boolean
	 */
	public void setDebug(boolean on) {
		this.debug = on;
	}
}
