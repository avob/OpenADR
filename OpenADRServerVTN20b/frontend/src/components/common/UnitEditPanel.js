import React from 'react';
import TextField from '@material-ui/core/TextField';
import InputAdornment from '@material-ui/core/InputAdornment';
import Clear from '@material-ui/icons/Clear';
import Done from '@material-ui/icons/Done';

import EnhancedAutocomplete from './EnhancedAutocomplete'
import Grid from '@material-ui/core/Grid';
import Checkbox from '@material-ui/core/Checkbox';
import InputLabel from '@material-ui/core/InputLabel';

import { makeStyles } from '@material-ui/core/styles';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import UnitPanel from '../common/UnitPanel'

const useStyles = makeStyles((theme) => ({
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
    minWidth: "300px"
  },
}));

var TransitionsModal = (props) => {
  const {definition, filterUnit, onUnitChange, unit} = props;
  const [value, setValue] = React.useState(false);
  const classes = useStyles();
  const [open, setOpen] = React.useState(false);

  var itemDescription = (unit && unit.itemDescription) ? unit.itemDescription : "No Unit"
  var itemUnits = (unit && unit.itemUnits) ? unit.itemUnits : "No Unit"
  var siScaleCode = (unit && unit.siScaleCode) ? unit.siScaleCode : "none"

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <div>
  
      <div onClick={handleOpen} style={{cursor:'pointer'}}>
      <UnitPanel unit={props.unit}/>
      </div>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        className={classes.modal}
        open={open}
        onClose={handleClose}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={open}>
          <div className={classes.paper}>
         	   
			      	<EnhancedAutocomplete
			            creatable
			            options={definition.itemDescription}
			            value={itemDescription}
			            label="Item Description"
			            onSelected={(value) => {
			            	filterUnit(value)
			            	onUnitChange({
			            		itemDescription: value,
			            		itemUnits: "No Unit",
			            		siScaleCode: "none"
			            	});
			            }}
			          />
		
			          <EnhancedAutocomplete
				        creatable
				        disabled={itemDescription == "No Unit"}
				        options={!value ? definition.itemUnits : definition.itemUnitsCurrency}
				        value={itemUnits}
				        label = "Item Units"
				        onSelected={(value) => {
			            	unit.itemUnits = value;
			            	onUnitChange(unit);
			            }}
				      />
		
				      <EnhancedAutocomplete
			            creatable
			            disabled={itemDescription == "No Unit"}
			            options={definition.siScaleCode}
			            value={siScaleCode}
			            label = "SI Scale Code"
			            onSelected={(value) => {
			            	unit.siScaleCode = value;
			            	onUnitChange(unit);
			            }}
			          />
			
          </div>
        </Fade>
      </Modal>
    </div>
  );
}

var UnitEditPanel = ( props ) => {
  return (
  	  
      <TransitionsModal {...props} />
  );
}

export default UnitEditPanel;

