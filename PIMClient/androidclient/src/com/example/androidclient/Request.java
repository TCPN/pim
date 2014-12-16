package com.example.androidclient;

import java.io.Serializable;



class Request implements Serializable
{
	transient String reqname ; //enum?
}

class CreateRequest extends Request implements Serializable
{
	String reqname = "" ;
	transient PJ data = null ;
	public CreateRequest(String reqname, PJ data)
	{
		this.reqname = reqname ;
		this.data = data ;
	}
	String getreqname()
	{
		return this.reqname ;
	}
	PJ getData()
	{
		return this.data ;
	}
}
class UpdateRequest extends Request implements Serializable
{
	Object data ;
	int[] address = new int[8] ; 
	public UpdateRequest(String reqname, int[] address, Object data)
	{
		this.reqname = reqname ;
		this.data = data ;
		this.address = address ;
	}
	String getreqname()
	{
		return this.reqname ;
	}
	Object getData()
	{
		return this.data ;
	}
	int[] getAddress()
	{
		return this.address ;
	}
}
class ReadRequest extends Request implements Serializable
{
	int[] address = new int[8]; 
	public ReadRequest(String reqname, int[] address)
	{
		this.reqname = reqname ;
		this.address = address ;
	}
	String getreqname()
	{
		return this.reqname ;
	}
	int[] getAddress()
	{
		return this.address ;
	}
}

