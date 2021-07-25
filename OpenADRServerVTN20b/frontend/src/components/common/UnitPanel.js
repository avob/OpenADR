import React from 'react';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import Clear from '@material-ui/icons/Clear';
import Done from '@material-ui/icons/Done';

var UnitPanel = ( props ) => {
  return (
      <React.Fragment>
      { props.unit && props.unit.itemDescription != "No Unit"? 
          <span>{props.unit.itemDescription}<br/>{props.unit.siScaleCode ? <React.Fragment>{props.unit.siScaleCode != "none" ? props.unit.siScaleCode + props.unit.itemUnits : props.unit.itemUnits}</React.Fragment> : null}</span> 
          : <span>No unit</span> }
      </React.Fragment>
  );
}

export default UnitPanel;

