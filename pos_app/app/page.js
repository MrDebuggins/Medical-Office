'use client';

import styles from "./page.module.css";
import {Login} from "./auth/login"
import { Profile } from "./auth/profile";
import { Patients } from "./views/patients";
import { Doctors } from "./views/doctors";
import { useState } from 'react';


var SetState;
var list;

export default function Home() 
{
  const [state, setState] = useState(0);
  SetState = setState;
    
  switch(state)
  {
    case 0:
    {
      return(
        <main className={styles.main}>
          <Login setState={setState}/>
        </main>
      );
    }
    case 1:
    {
      return(
        <main className={styles.main}>
          <Profile setState={setState}/>
        </main>
      );
    }
    case 2:
    {
      return(
        <main className={styles.main}>
          <Patients setState={setState}/>
        </main>
      );
    }
    case 3:
    {
      return(
        <main className={styles.main}>
          <Doctors setState={setState}/>
        </main>
      );
    }
  }
}