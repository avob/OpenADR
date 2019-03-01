import React from 'react';

import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardActions from '@material-ui/core/CardActions';
import CardContent from '@material-ui/core/CardContent';
import CardMedia from '@material-ui/core/CardMedia';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import GridList from '@material-ui/core/GridList';
import GridListTile from '@material-ui/core/GridListTile';

import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import FormGroup from '@material-ui/core/FormGroup';



import TextField from '@material-ui/core/TextField';
import AddIcon from '@material-ui/icons/Add';

import Divider from '@material-ui/core/Divider';







import green from '@material-ui/core/colors/green';
import blue from '@material-ui/core/colors/blue';




function VtnConfigurationMarketContextCard(props) {
  return (
  	<GridListTile cols={1} row={1}>
   <Card className={props.classes.card} >
	   <CardContent
	       	  className={props.classes.media}
	          style={props.color}
	        />
	    <CardContent>
	      <Typography gutterBottom variant="h5" component="h2">
	       {props.context.name}
	      </Typography>

	      <Typography component="p">
	        {props.context.description}
	      </Typography>
	    </CardContent>
	  <CardActions>
	 
	    <Button size="small" color="primary">
	      Edit
	    </Button>
	    <Button size="small" color="primary">
	      Events
	    </Button>
	    <Button size="small" color="primary">
	      Ven
	    </Button>
	     <Button size="small" color="primary">
	      Delete
	    </Button>
	  </CardActions>
	</Card>
	</GridListTile>

  );
}

export class VtnConfigurationMarketContext extends React.Component {
// const VtnConfigurationMarketContext = (props) => {
	constructor(props) {
		super(props);

		this.state = {}

		 this.handleCreateMarketContextButtonClick = this.handleCreateMarketContextButtonClick.bind(this);

	}

	
	handleCreateMarketcontextNameChange = (event) => {
	    this.setState({ name:event.target.value });
	};

	handleCreateMarketcontextNameDescription = (event) => {
	    this.setState({ description:event.target.value });
	};

	handleCreateMarketContextButtonClick(event) {
		event.preventDefault();

		this.props.createMarketContext({
			name: this.state.name,
			description: this.state.description
		})

		this.setState({name:"", description: ""})
	}

	render() {
		const { classes, marketContext } = this.props;
	  var view = [
	  ];

	  var j = 2;
	  for(var i in marketContext) {
	  	var context = marketContext[i];
	  	var color = {backgroundColor: blue[Object.keys(blue)[j ]]}
	  	
	  	view.push(
	  		<VtnConfigurationMarketContextCard key={"marketcontext_card_"+context.id} classes={classes} context={context} color={color}/>
	  	);
	  	j++;
	  }

	  const flexContainer = {
	  display: 'flex',
	  flexDirection: 'row',
	};
  return (
  	<div>
  		<Divider variant="middle"  style= {{
    marginBottom:"40px"
  }}/>
  		<FormLabel component="legend">Create New Market Context</FormLabel>
  		<form style={flexContainer}>

		<FormGroup row={true}>	  
  		
  		<TextField
          label="Name"
          placeholder="Name"
          value={this.state.name}
          className={classes.textField}
          margin="normal"
          onChange={this.handleCreateMarketcontextNameChange}
        
        />
        <TextField
          label="Description"
          placeholder="Description"
          value={this.state.description}
          className={classes.textField}
          margin="normal"
         onChange={this.handleCreateMarketcontextNameDescription}
        />
        <Button variant="outlined" color="primary" size="small"  className={classes.button} style={{marginTop:"30px"}} onClick={this.handleCreateMarketContextButtonClick}>
        <AddIcon/> Add
      </Button>
        </FormGroup>
        </form>
        <div>
        <Divider variant="middle"  style= {{
    marginBottom:"40px",
    marginTop:"20px"
  }}/>
  <FormLabel component="legend" style= {{
    marginBottom:"40px",
    marginTop:"20px"
  }}><strong>Existing Market Contexts</strong></FormLabel>
  	<GridList style= {{
    justifyContent: 'space-around',
  }} >
  		{view}
  	</GridList>
  	</div>
  	</div>
  );
}
}

export default VtnConfigurationMarketContext;
