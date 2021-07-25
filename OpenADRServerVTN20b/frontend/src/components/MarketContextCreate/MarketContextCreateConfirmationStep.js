import React from 'react';







import TextField from '@material-ui/core/TextField';



import Grid from '@material-ui/core/Grid';


import Avatar from '@material-ui/core/Avatar';


export class MarketContextCreateConfirmationStep extends React.Component {

  render() {
    const {classes, marketContext, hasError} = this.props;


    console.log(marketContext)
    return (
      <React.Fragment>
      <Grid container style={{margin: "20px 0"}}>
          <Grid item xs={ 2 } />
          <Grid item xs={ 4 }>
            <TextField required disabled
                       helperText="Name must be unique"
                       label="Name"
                       value={ marketContext.name }
                       className={ classes.textField }
                       fullWidth={ true } 
                       error={ hasError }
                       InputProps={{
                          readOnly: true,
                        }}
                       InputLabelProps={ { shrink: true, } } />
          </Grid>
          <Grid item xs={ 1 } />
          <Grid item xs={ 1 }>
           
           </Grid>
          <Grid item xs={ 2 }>
            <TextField label="Color"
                      disabled
                       value={ marketContext.color }
                       className={ classes.textField }
                       fullWidth={ true } 
                       error={ hasError }
                       helperText= "Market context color"
                       InputProps={{
                          readOnly: true,
                          style:{color: marketContext.color }
                        }}
                       InputLabelProps={ { shrink: true, } } />

           </Grid>
            <Grid item xs={ 1 }>

            <Avatar style={{backgroundColor: marketContext.color, width: "15px", height: "15px", top: "25px"}}/>
           </Grid>
       
        </Grid>
        <Grid container  style={{margin: "20px 0"}}>
          <Grid item xs={ 2 } />

          <Grid item xs={ 8 }>
            <TextField disabled
                        label="Description"
                       value={ marketContext.description }
                       className={ classes.textField }
                       multiline
                       fullWidth={ true }
                       InputLabelProps={ { shrink: true, } } />



      
                       
    
          </Grid>
 
  
        </Grid>
    

        </React.Fragment>
 
    );
  }
}

export default MarketContextCreateConfirmationStep;
