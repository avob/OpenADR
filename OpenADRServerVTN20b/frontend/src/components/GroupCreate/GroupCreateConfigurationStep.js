import React from 'react';







import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';



import ColorPicker from 'material-ui-color-picker'



import Divider from '@material-ui/core/Divider';

import Avatar from '@material-ui/core/Avatar';


export class GroupCreateConfigurationStep extends React.Component {

  onNameChange = (event) => {
    this.props.handleConfigurationChange({name: event.target.value, description: this.props.group.description,  color: this.props.group.color})
  }

  onDescriptionChange = (event) => {
    this.props.handleConfigurationChange({name: this.props.group.name, description: event.target.value,  color: this.props.group.color})
  }

  onColorChange = (color) => {
    this.props.handleConfigurationChange({name: this.props.group.name, description: this.props.group.description,  color: color})
  }

  render() {
    const {classes, group, hasError} = this.props;

    return (
      <React.Fragment>
      <Grid container style={{margin: "20px 0"}}>
          <Grid item xs={ 2 } />
          <Grid item xs={ 4 }>
            <TextField required
                       helperText="Name must be unique"
                       label="Name"
                       value={ group.name }
                       className={ classes.textField }
                       fullWidth={ true } 
                       error={ hasError }
                       onChange={this.onNameChange}
                       InputLabelProps={ { shrink: true, } } />
          </Grid>
          <Grid item xs={ 1 } />
          <Grid item xs={ 1 }>
           
           </Grid>
          <Grid item xs={ 2 }>
           <ColorPicker label="Color"
                           defaultValue='#000'

                           value={group.color}
                           TextFieldProps={ { 
                            className: classes.textField
                            , helperText: "Market context color"
                            , InputLabelProps: {
                              shrink: true,
                            },
                            InputProps:{style:{color: group.color}}
                            } } 
                           onChange={ this.onColorChange } />

           </Grid>
            <Grid item xs={ 1 }>

            <Avatar style={{backgroundColor: group.color, width: "15px", height: "15px", top: "25px"}}/>
           </Grid>
       
        </Grid>
        <Grid container  style={{margin: "20px 0"}}>
          <Grid item xs={ 2 } />

          <Grid item xs={ 8 }>
            <TextField label="Description"
                       value={ group.description }
                       className={ classes.textField }
                       multiline
                       fullWidth={ true }
                       onChange={this.onDescriptionChange} 
                       InputLabelProps={ { shrink: true, } } />



      
                       
    
          </Grid>
 
  
        </Grid>
    

        </React.Fragment>
 
    );
  }
}

export default GroupCreateConfigurationStep;
