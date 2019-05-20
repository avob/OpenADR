import React from 'react';

import Button from '@material-ui/core/Button';

import GridList from '@material-ui/core/GridList';









import TextField from '@material-ui/core/TextField';
import AddIcon from '@material-ui/icons/Add';
import CloseIcon from '@material-ui/icons/Close';







import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';











import { VtnConfigurationGroupCard } from '../common/VtnConfigurationCard'




export class VtnConfigurationGroup extends React.Component {
  constructor( props ) {
    super( props );

    this.state = {}
    this.state.name = ''
    this.state.description = ''
    this.state.editMode = false;


  }


  handleGroupNameChange = (event) => {
    this.setState( {
      name: event.target.value
    } );
  };

  handleGroupDescriptionChange = (event) => {
    this.setState( {
      description: event.target.value
    } );
  };

  handleCancelGroupButtonClick = () => {
    this.setState( {
      name: '',
      description: '',
      color: '',
      editMode: false
    } )
  }

  handleSaveGroupButtonClick = (event) => {
    event.preventDefault();
    var dto = {
      name: this.state.name,
      description: this.state.description,
    }
    if ( !this.state.editMode ) {
      this.props.createGroup( dto )
    } else {
      this.props.updateGroup( dto )
    }


    this.setState( {
      name: '',
      description: '',
      editMode: false
    } )
  }

  handleDeleteGroup = (id) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.props.deleteGroup( id );
    }

  }

  handleEditGroup = (context) => {
    var that = this;
    return function ( event ) {
      event.preventDefault();
      that.setState( {
        editMode: true,
        name: context.name,
        description: context.description || "",
        color: context.color
      } )
    }
  }



  render() {
    const {classes, group} = this.props;
    var view = [];

    for (var i in group) {
      var g = group[ i ];

      view.push(

        <VtnConfigurationGroupCard key={ 'group_card_' + g.id }
                                   classes={ classes }
                                   group={ g }
                                   handleDeleteGroup={ this.handleDeleteGroup( g.id ) }
                                   handleEditGroup={ this.handleEditGroup( g ) } />
      );
    }

    var marginTop = 13;

    return (
    <div>
      <form className={ classes.root }>
        <Grid container spacing={ 8 }>
          <Grid container>
            <Grid item xs={ 3 }>
              <TextField label="Name"
                         value={ this.state.name }
                         className={ classes.textField }
                         onChange={ this.handleGroupNameChange }
                         disabled={ this.state.editMode }
                         style={{width:"95%"}}
                          InputLabelProps={{
                          shrink: true,
                        }}
                        InputProps={{style:{marginTop:24}}} />
            </Grid>
            <Grid item xs={ 7 }>
              <TextField label="Description"
                         value={ this.state.description }
                         className={ classes.textField }
                         onChange={ this.handleGroupDescriptionChange }
                         style={{width:"95%"}}
                          InputLabelProps={{
                          shrink: true,
                        }}
                        InputProps={{style:{marginTop:24, }}} />
            </Grid>
            <Grid item xs={ 1 }>
              

              {(this.state.editMode) ? <Button key="vtn_cancel"
                        variant="outlined"
                        color="secondary"
                        size="small"
                        className={ classes.button }
                        style={ { marginTop } }
                        onClick={ this.handleCancelGroupButtonClick }>
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
                              onClick={ this.handleCreateGroupButtonClick }>
                        <AddIcon /> Save
                      </Button> : null}

              {(!this.state.editMode) ? <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop } }
                            fullWidth={ true } 
                            onClick={ this.handleCreateGroupButtonClick }>
                      <AddIcon />New
                    </Button>: null}

            </Grid>
          </Grid>
        </Grid>
      </form>
      <div>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <GridList style={ { justifyContent: 'left', } }>
          { view }
        </GridList>
      </div>
    </div>
    );
  }
}

export default VtnConfigurationGroup;
