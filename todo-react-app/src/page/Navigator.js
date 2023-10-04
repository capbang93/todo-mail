import { AppBar, Button, Grid, Toolbar, Typography } from '@material-ui/core'
import React from 'react'
import { Link } from 'react-router-dom'
import { signout } from '../service/ApiService'

function Navigator() {
  return (
      // navigationBar
    <AppBar position="static">
      <Toolbar>
        <Grid justifyContent="space-between" container>
          <Grid item><div style={{cursor:"pointer"}} onClick={()=>window.location.href="/"}>
          <Typography variant="h6">오늘의 할일</Typography>
          </div>
          </Grid>
          <Grid item>
          <Button color="inherit" href="/profile">
              profile
            </Button>
            <Button color="inherit" onClick={signout}>
              logout
            </Button>
          </Grid>
        </Grid>
      </Toolbar>
    </AppBar>
  )
}

export default Navigator