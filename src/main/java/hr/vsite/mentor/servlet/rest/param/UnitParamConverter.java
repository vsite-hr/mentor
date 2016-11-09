package hr.vsite.mentor.servlet.rest.param;

import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.transaction.Transactional;
import javax.ws.rs.ext.ParamConverter;

import org.apache.commons.lang3.StringUtils;

import hr.vsite.mentor.unit.Unit;
import hr.vsite.mentor.unit.UnitManager;

public class UnitParamConverter implements ParamConverter<Unit> {

	@Inject
	UnitParamConverter(Provider<UnitManager> unitProvider) {
		this.unitProvider = unitProvider;
	}
	
	@Override
	@Transactional
	public Unit fromString(String id) {

		if (StringUtils.isBlank(id))
			return null;
		
		return unitProvider.get().findById(UUID.fromString(id));
		
	}

	@Override
	public String toString(Unit unit) {
		if (unit == null)
			return null;
		return unit.getId().toString();
	}

	private final Provider<UnitManager> unitProvider;
	
}
