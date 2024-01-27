package hr.algebra.rickandmortyapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.rickandmortyapp.R
import hr.algebra.rickandmortyapp.adapter.CharacterAdapter
import hr.algebra.rickandmortyapp.databinding.FragmentCharactersBinding
import hr.algebra.rickandmortyapp.framework.fetchItems
import hr.algebra.rickandmortyapp.model.Character

class CharactersFragment : Fragment() {
    private lateinit var items: MutableList<Character>
    private lateinit var binding: FragmentCharactersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        items = requireContext().fetchItems()
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvCharacters.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = CharacterAdapter(requireContext(), items)
        }
    }
}