package com.opentext.explore.importer.excel.fieldhandlers;

import com.opentext.explore.importer.excel.pojo.TextData;

import junit.framework.TestCase;

public abstract class AbstractTestFieldHandler extends TestCase {

	protected TextData txtData;

	public AbstractTestFieldHandler() {
		super();
	}

	public AbstractTestFieldHandler(String name) {
		super(name);
	}

	@Override
	protected void setUp() {
		txtData = new TextData();
		txtData.setReferenceId("1583916");
		txtData.setInteractionId("1583916");
		txtData.setId("1583916");
		txtData.setContent("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
		txtData.addField("ComentariosOficina", "Integer finibus leo purus, quis porttitor quam aliquet ut.");
	}
}