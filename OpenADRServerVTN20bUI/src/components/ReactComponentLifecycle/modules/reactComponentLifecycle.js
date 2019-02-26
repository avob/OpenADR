export const COMPONENT_UNMOUNT = 'COMPONENT_UNMOUNT'

export const unmount = () => {

  return (dispatch, getState) => {
    dispatch({type:COMPONENT_UNMOUNT});
  }
}

// ------------------------------------
// Action Handlers
// ------------------------------------
export function actionHandlers(actionHandler, initialState){
	actionHandler[COMPONENT_UNMOUNT] = (state, action) => {
		return initialState; 
	}
	return actionHandler;
}
