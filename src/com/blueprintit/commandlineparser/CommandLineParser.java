package com.blueprintit.commandlineparser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Dave
 */
public class CommandLineParser
{
	private List boolopts;
	private Map valopts;
	private List booloptnames;
	private List valoptnames;
	private Map optnamemap;
	private List unknown;
	
	public CommandLineParser()
	{
		booloptnames = new LinkedList();
		valoptnames = new LinkedList();
		optnamemap = new HashMap();
	}
	
	public void addBooleanOption(String longopt)
	{
		addBooleanOption(longopt,null);
	}
	
	public void addBooleanOption(String longopt, String shortopt)
	{
		if (shortopt!=null)
		{
			optnamemap.put(shortopt,longopt);
		}
		booloptnames.add(longopt);			
	}
	
	public void addValueOption(String longopt)
	{
		addBooleanOption(longopt,null);
	}
	
	public void addValueOption(String longopt, String shortopt)
	{
		if (shortopt!=null)
		{
			optnamemap.put(shortopt,longopt);
		}
		valoptnames.add(longopt);			
	}
	
	public void parse(String[] args)
	{
		boolopts = new LinkedList();
		valopts = new HashMap();
		unknown = new LinkedList();
		
		int pos = 0;
		while (pos<args.length)
		{
			if (args[pos].startsWith("--"))
			{
				String nv = args[pos].substring(2);
				if (nv.indexOf("=")>0)
				{
					String name = nv.substring(0,nv.indexOf("="));
					String value = nv.substring(nv.indexOf("=")+1);
					if (valoptnames.contains(name))
					{
						valopts.put(name,value);
					}
					else
					{
						unknown.add(args[pos]);
					}
				}
				else if (booloptnames.contains(nv))
				{
					boolopts.add(nv);
				}
				else
				{
					unknown.add(args[pos]);
				}
			}
			else if (args[pos].charAt(0)=='-')
			{
				String name = args[pos].substring(1);
				if (optnamemap.containsKey(name))
				{
					name=(String)optnamemap.get(name);
				}
				if (booloptnames.contains(name))
				{
					boolopts.add(name);
				}
				else if (valoptnames.contains(name))
				{
					pos++;
					valopts.put(name,args[pos]);
				}
				else
				{
					unknown.add(args[pos]);
				}
			}
			else
			{
				unknown.add(args[pos]);
			}
			pos++;
		}
	}
	
	public List getUnknownOptions()
	{
		return unknown;
	}
	
	public boolean getBooleanOption(String option)
	{
		return boolopts.contains(option);
	}
	
	public boolean hasOption(String option)
	{
		return boolopts.contains(option)||(valopts.containsKey(option));
	}
	
	public int getIntOption(String option)
	{
		String value = getStringOption(option);
		if (value!=null)
		{
			return Integer.parseInt(value);
		}
		return -1;
	}
	
	public String getOption(String option)
	{
		return getStringOption(option);
	}
	
	public String getStringOption(String option)
	{
		return (String)valopts.get(option);
	}
}
