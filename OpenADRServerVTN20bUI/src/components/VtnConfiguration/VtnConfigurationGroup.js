import React from 'react';

import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';
import ColorPicker from 'material-ui-color-picker'
import IconButton from '@material-ui/core/IconButton';


import TextField from '@material-ui/core/TextField';
import AddIcon from '@material-ui/icons/Add';
import CloseIcon from '@material-ui/icons/Close';
import CancelIcon from '@material-ui/icons/Cancel';


import ExtensionIcon from '@material-ui/icons/Extension';



import Divider from '@material-ui/core/Divider';

import Grid from '@material-ui/core/Grid';



import Paper from '@material-ui/core/Paper';




import green from '@material-ui/core/colors/green';
import blue from '@material-ui/core/colors/blue';

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

  handleCancelMarketContextButtonClick = () => {
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
        description: context.description,
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

    const flexContainer = {
      display: 'flex',
      flexDirection: 'row',
    };
    var saveBtnView = null;
    var marginTop = 13;
    if ( !this.state.editMode ) {
      saveBtnView = <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop } }
                            onClick={ this.handleSaveGroupButtonClick }>
                      <AddIcon />Create
                    </Button>
    } else {
      saveBtnView = [ <Button key="btn_save"
                              variant="outlined"
                              color="primary"
                              size="small"
                              className={ classes.button }
                              style={ { marginTop } }
                              onClick={ this.handleSaveGroupButtonClick }>
                        <AddIcon />Save
                      </Button>,
        <Button key="vtn_cancel"
                variant="outlined"
                color="secondary"
                size="small"
                className={ classes.button }
                style={ { marginTop } }
                onClick={ this.handleCancelMarketContextButtonClick }>
          <CloseIcon />Cancel
        </Button>
      ]
    }


    return (
    <div>
      <form style={ flexContainer }>
        <Grid container spacing={ 8 }>
          <Grid container
                item
                xs={ 12 }
                spacing={ 24 }>
            <Grid item xs={ 2 }>
              <TextField label="Name"
                         value={ this.state.name }
                         className={ classes.textField }
                         onChange={ this.handleGroupNameChange }
                         disabled={ this.state.editMode }
                          InputLabelProps={{
                          shrink: true,
                        }}
                        InputProps={{style:{marginTop:24}}} />
            </Grid>
            <Grid item xs={ 4 }>
              <TextField label="Description"
                         value={ this.state.description }
                         className={ classes.textField }
                         onChange={ this.handleGroupDescriptionChange }
                         fullWidth={ true }
                          InputLabelProps={{
                          shrink: true,
                        }}
                        InputProps={{style:{marginTop:24}}} />
            </Grid>
            <Grid item xs={ 4 }>
              { saveBtnView }
            </Grid>
          </Grid>
        </Grid>
      </form>
      <div>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <GridList style={ { justifyContent: 'space-around', } }>
          { view }
        </GridList>
      </div>
    </div>
    );
  }
}

export default VtnConfigurationGroup;
