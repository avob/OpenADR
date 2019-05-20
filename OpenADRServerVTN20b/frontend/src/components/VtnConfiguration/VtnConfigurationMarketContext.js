import React from 'react';

import Button from '@material-ui/core/Button';

import GridList from '@material-ui/core/GridList';





import ColorPicker from 'material-ui-color-picker'



import TextField from '@material-ui/core/TextField';
import AddIcon from '@material-ui/icons/Add';
import CloseIcon from '@material-ui/icons/Close';





import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';

import { VtnConfigurationMarketContextCard } from '../common/VtnConfigurationCard'




export class VtnConfigurationMarketContext extends React.Component {
  constructor( props ) {
    super( props );

    this.state = {}
    this.state.name = ''
    this.state.description = ''
    this.state.color = '';
    this.state.editMode = false;


  }


  handleCreateMarketContextNameChange = (event) => {
    this.setState( {
      name: event.target.value
    } );
  };

  handleCreateMarketContextDescriptionChange = (event) => {
    this.setState( {
      description: event.target.value
    } );
  };

  handleCreateMarketContextColorChange = (color) => {
    this.setState( {
      color: color
    } );
  };

  handleCancelMarketContextButtonClick = () => {
    this.setState( {
      name: '',
      description: '',
      color: '',
      editMode: false
    } )
  }

  handleCreateMarketContextButtonClick = (event) => {
    event.preventDefault();
    var dto = {
      name: this.state.name,
      description: this.state.description,
      color: this.state.color
    }
    if ( !this.state.editMode ) {
      this.props.createMarketContext( dto )
    } else {
      this.props.updateMarketContext( dto )
    }


    this.setState( {
      name: '',
      description: '',
      color: '',
      editMode: false
    } )
  }

  handleDeleteMarketContext = (id) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.props.deleteMarketContext( id );
    }

  }

  handleEditMarketContext = (context) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.setState( {
        editMode: true,
        name: context.name,
        description: context.description,
        color: context.color
      } )
    }
  }



  render() {
    const {classes, marketContext} = this.props;
    var view = [];

    for (var i in marketContext) {
      var context = marketContext[ i ];

      view.push(

        <VtnConfigurationMarketContextCard key={ 'marketcontext_card_' + context.id }
                                           classes={ classes }
                                           context={ context }
                                           handleDeleteMarketContext={ this.handleDeleteMarketContext( context.id ) }
                                           handleEditMarketContext={ this.handleEditMarketContext( context ) } />
      );
    }

    var marginTop = 13;



    return (
    <div className={ classes.root }>
      <form >
        <Grid container spacing={ 8 }>
          <Grid container>
                
            <Grid item xs={ 3 }>
              <TextField label="Name"
                         value={ this.state.name }
                         className={ classes.textField }
                         onChange={ this.handleCreateMarketContextNameChange }
                         disabled={ this.state.editMode }
                         style={{width:"95%"}}
                         InputLabelProps={{
                          shrink: true,
                        }}

                        InputProps={{style:{marginTop:24}}} />
            </Grid>
            <Grid item xs={ 5 }>
              <TextField label="Description"
                         value={ this.state.description }
                         className={ classes.textField }
                         style={{width:"95%"}}
                         onChange={ this.handleCreateMarketContextDescriptionChange }
                         fullWidth={ true } 
                         InputLabelProps={{
                            shrink: true,
                          }}
                          InputProps={{style:{marginTop:24}}}/>
            </Grid>
            <Grid item xs={ 2 }>
              <ColorPicker label="Color"
                           defaultValue='#000'
                           value={ this.state.color }
                           TextFieldProps={ { 
                            className: classes.textField
                            , style:{width:"95%"}
                            , InputLabelProps: {
                              shrink: true,
                            },
                            InputProps:{style:{marginTop:24, color: this.state.color}}
                            } } 
                           onChange={ this.handleCreateMarketContextColorChange } />
            </Grid>
            <Grid item xs={ 1 }>
              

              {(this.state.editMode) ? <Button key="vtn_cancel"
                        variant="outlined"
                        color="secondary"
                        size="small"
                        className={ classes.button }
                        style={ { marginTop } }
                        onClick={ this.handleCancelMarketContextButtonClick }>
                  <CloseIcon />
                </Button>: null}      
            </Grid>
            <Grid item xs={ 1 }>
              {(this.state.editMode) ? <Button key="btn_save"
                              variant="outlined"
                              color="primary"
                              size="small"
                              className={ classes.button }
                              style={ { marginTop } }
                              fullWidth={ true } 
                              onClick={ this.handleCreateMarketContextButtonClick }>
                        <AddIcon /> Save
                      </Button> : null}

              {(!this.state.editMode) ? <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop } }
                            fullWidth={ true } 
                            onClick={ this.handleCreateMarketContextButtonClick }>
                      <AddIcon />New
                    </Button>: null}

            </Grid>


            
          </Grid>
        </Grid>
      </form>
      <div >
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <GridList style={ { justifyContent: 'left', } }>
          { view }
        </GridList>
      </div>
    </div>
    );
  }
}

export default VtnConfigurationMarketContext;
