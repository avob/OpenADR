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

import { VtnConfigurationMarketContextCard } from '../common/VtnConfigurationCard'




export class VtnConfigurationMarketContext extends React.Component {
  // const VtnConfigurationMarketContext = (props) => {
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

    const flexContainer = {
      display: 'flex',
      flexDirection: 'row',
    };
    var saveBtnView = null;
    var formLabelText = null;
    var marginTop = 13;
    if ( !this.state.editMode ) {
      formLabelText = 'Create New Market Context';
      saveBtnView = <Button key="btn_create"
                            variant="outlined"
                            color="primary"
                            size="small"
                            className={ classes.button }
                            style={ { marginTop } }
                            onClick={ this.handleCreateMarketContextButtonClick }>
                      <AddIcon />Create
                    </Button>
    } else {
      formLabelText = 'Edit Market Context';
      saveBtnView = [ <Button key="btn_save"
                              variant="outlined"
                              color="primary"
                              size="small"
                              className={ classes.button }
                              style={ { marginTop } }
                              onClick={ this.handleCreateMarketContextButtonClick }>
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
      <Typography gutterBottom
                  variant="title"
                  component="h2">
        { formLabelText }
      </Typography>
      <form style={ flexContainer }>
        <Grid container spacing={ 8 }>
          <Grid container
                item
                xs={ 12 }
                spacing={ 24 }>
            <Grid item xs={ 2 }>
              <TextField label="Name"
                         placeholder="Name"
                         value={ this.state.name }
                         className={ classes.textField }
                         onChange={ this.handleCreateMarketContextNameChange }
                         disabled={ this.state.editMode } />
            </Grid>
            <Grid item xs={ 4 }>
              <TextField label="Description"
                         placeholder="Description"
                         value={ this.state.description }
                         className={ classes.textField }
                         onChange={ this.handleCreateMarketContextDescriptionChange }
                         fullWidth={ true } />
            </Grid>
            <Grid item xs={ 2 }>
              <ColorPicker label="Color"
                           placeholder="Color"
                           defaultValue='#000'
                           value={ this.state.color }
                           TextFieldProps={ { className: classes.textField, fullWidth: true } }
                           onChange={ this.handleCreateMarketContextColorChange } />
            </Grid>
            <Grid item xs={ 4 }>
              { saveBtnView }
            </Grid>
          </Grid>
        </Grid>
      </form>
      <div>
        <Divider style={ { marginBottom: '20px', marginTop: '20px' } } />
        <Typography gutterBottom
                    variant="title"
                    component="h2">
          Existing Market Contexts
        </Typography>
        <GridList style={ { justifyContent: 'space-around', } }>
          { view }
        </GridList>
      </div>
    </div>
    );
  }
}

export default VtnConfigurationMarketContext;
